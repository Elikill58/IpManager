package com.elikill58.ipmanager.universal.dataStorage;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ipmanager.universal.account.IpManagerAccount;

public final class VoidAccountStorage extends IpManagerAccountStorage {

	public static final VoidAccountStorage INSTANCE = new VoidAccountStorage();

	@Override
	public CompletableFuture<@Nullable IpManagerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> saveAccount(IpManagerAccount account) {
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public List<UUID> getPlayersOnIP(String ip) {
		return Collections.emptyList();
	}
}
