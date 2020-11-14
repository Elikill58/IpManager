package com.elikill58.ipmanager.universal.dataStorage;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ipmanager.universal.account.IpPlayerAccount;

public final class VoidAccountStorage extends IpManagerAccountStorage {

	public static final VoidAccountStorage INSTANCE = new VoidAccountStorage();

	@Override
	public CompletableFuture<@Nullable IpPlayerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> saveAccount(IpPlayerAccount account) {
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public List<UUID> getPlayersOnIP(String ip) {
		return Collections.emptyList();
	}
}
