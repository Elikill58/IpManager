package com.elikill58.ipmanager.universal.dataStorage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.dataStorage.database.DatabaseIpAccountStorage;
import com.elikill58.ipmanager.universal.dataStorage.file.FileIpAccountStorage;

public abstract class IpManagerAccountStorage {

	private static final Map<String, IpManagerAccountStorage> storages = new HashMap<>();
	private static String storageId;

	public abstract CompletableFuture<IpPlayerAccount> loadAccount(UUID playerId);

	public abstract CompletableFuture<Void> saveAccount(IpPlayerAccount account);

	public CompletableFuture<IpPlayerAccount> getOrCreateAccount(UUID playerId) {
		return loadAccount(playerId).thenApply(existingAccount -> existingAccount == null ? new IpPlayerAccount(playerId) : existingAccount);
	}

	public abstract List<UUID> getPlayersOnIP(String ip);

	public static IpManagerAccountStorage getStorage() {
		return storages.getOrDefault(storageId, VoidAccountStorage.INSTANCE);
	}

	public static void register(String id, IpManagerAccountStorage storage) {
		storages.put(id, storage);
	}

	public static String getStorageId() {
		return storageId;
	}

	public static void setStorageId(String storageId) {
		IpManagerAccountStorage.storageId = storageId;
	}

	public static void setDefaultStorage(String storageId) {
		IpManagerAccountStorage storage = storages.get(storageId);
		if (storage != null) {
			register("default", storage);
		}
	}

	public static void init() {
		Adapter adapter = Adapter.getAdapter();
		storageId = adapter.getConfig().getString("accounts.storage.id");
		
		IpManagerAccountStorage.register("file", new FileIpAccountStorage(new File(adapter.getDataFolder(), "user")));
		if (Database.hasCustom) {
			IpManagerAccountStorage.register("database", new DatabaseIpAccountStorage());
		}
	}
}
