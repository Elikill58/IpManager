package com.elikill58.ipmanager.spigot.impl.entity;

import java.util.UUID;

import com.elikill58.ipmanager.api.entity.OfflinePlayer;

public class SpigotOfflinePlayer extends OfflinePlayer {

	private final org.bukkit.OfflinePlayer op;
	
	public SpigotOfflinePlayer(org.bukkit.OfflinePlayer op) {
		this.op = op;
	}

	@Override
	public boolean isOp() {
		return op.isOp();
	}

	@Override
	public Object getDefault() {
		return op;
	}

	@Override
	public String getName() {
		return op.getName();
	}

	@Override
	public boolean isOnline() {
		return op.isOnline();
	}

	@Override
	public UUID getUniqueId() {
		return op.getUniqueId();
	}

	@Override
	public boolean hasPlayedBefore() {
		return op.hasPlayedBefore();
	}
}
