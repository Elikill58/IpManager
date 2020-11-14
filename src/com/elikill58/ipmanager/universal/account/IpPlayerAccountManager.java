package com.elikill58.ipmanager.universal.account;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.annotation.Nullable;

import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;

public class IpPlayerAccountManager {

	protected final Map<UUID, IpPlayerAccount> accounts = Collections.synchronizedMap(new HashMap<>());
	private final Map<UUID, CompletableFuture<IpPlayerAccount>> pendingRequests = Collections.synchronizedMap(new HashMap<>());
	private final boolean persistent;

	public IpPlayerAccountManager(boolean persistent) {
		this.persistent = persistent;
	}
	
	/**
	 * Gets the account associated to the given UUID wrapped in a CompletableFuture.
	 * <p>
	 * The account may be returned directly (if cached for example), or loaded via
	 * potential (slow) IO operations.
	 * <p>
	 * Ideally, cancelling the returned CompletableFuture will cancel the underlying
	 * (potentially slow) operations to avoid wasting resources.
	 *
	 * @param accountId the ID of the account to get
	 *
	 * @return the requested account wrapped in a CompletableFuture
	 *
	 * @see #getNow
	 */
	public CompletableFuture<IpPlayerAccount> get(UUID accountId) {
		CompletableFuture<IpPlayerAccount> pendingRequest = pendingRequests.get(accountId);
		if (pendingRequest != null) {
			return pendingRequest;
		}

		IpPlayerAccount existingAccount = accounts.get(accountId);
		if (existingAccount != null) {
			return CompletableFuture.completedFuture(existingAccount);
		}
		CompletableFuture<IpPlayerAccount> loadFuture = IpManagerAccountStorage.getStorage().getOrCreateAccount(accountId);
		pendingRequests.put(accountId, loadFuture);
		loadFuture.whenComplete((account, throwable) -> {
			pendingRequests.remove(accountId);
			if (throwable != null && !(throwable instanceof CancellationException)) {
				Adapter.getAdapter().getLogger().error("Account loading completed exceptionally: " + throwable.getMessage());
				throwable.printStackTrace();
				return;
			}

			UUID playerId = account.getPlayerId();
			accounts.put(playerId, account);
		});
		return loadFuture;
	}

	/**
	 * Gets the account associated to the given UUID in a blocking manner.
	 * <p>
	 * This is equivalent to calling {@code get(accountId).join()}
	 * <p>
	 * This method may throw {@link CompletionException} according to {@link CompletableFuture#join()},
	 * and is unlikely (but still may) throw {@link CancellationException}.
	 *
	 * @param accountId the ID of the account to get
	 *
	 * @return the requested account
	 */
	public IpPlayerAccount getNow(UUID accountId) {
		return get(accountId).join();
	}

	/**
	 * Saves the account associated to the given UUID.
	 * The returned CompletableFuture will be completed once the save has been done,
	 * and may complete exceptionally if it failed in some way.
	 *
	 * @param accountId the ID of the account to save
	 *
	 * @return a CompletableFuture that will complete once the save has been done
	 */
	public CompletableFuture<Void> save(UUID accountId) {
		if (persistent) {
			IpPlayerAccount existingAccount = accounts.get(accountId);
			if (existingAccount != null) {
				return IpManagerAccountStorage.getStorage().saveAccount(existingAccount);
			}
		}
		return CompletableFuture.completedFuture(null);
	}

	/**
	 * Makes this manager use the the given account's data instead of what it may have already.
	 *
	 * @param account the account to use
	 */
	public void update(IpPlayerAccount account) {
		accounts.put(account.getPlayerId(), account);
	}

	/**
	 * Indicates that the account for the given UUID can be forgotten by this manager.
	 * <p>
	 * What this method does is completely up to the implementing class, it may do nothing,
	 * or remove the account from a cache for example.
	 * <p>
	 * If an implementation caches accounts, the returned account would be the one removed from its cache.
	 *
	 * @param accountId the ID of the account to dispose
	 *
	 * @return the disposed account, if available
	 */
	@Nullable
	public IpPlayerAccount dispose(UUID accountId) {
		return accounts.remove(accountId);
	}
	
	private static IpPlayerAccountManager instance;
	
	public static IpPlayerAccountManager getManager() {
		if(instance == null)
			instance = new IpPlayerAccountManager(true);
		return instance;
	}
}
