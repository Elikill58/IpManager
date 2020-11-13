package com.elikill58.ipmanager.sponge.impl.entity;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.User;

import com.elikill58.ipmanager.api.entity.OfflinePlayer;

public class SpongeOfflinePlayer extends OfflinePlayer {

	private final User u;
	
	public SpongeOfflinePlayer(User u) {
		this.u = u;
	}

	@Override
	public UUID getUniqueId() {
		return u.getUniqueId();
	}

	@Override
	public boolean isOnline() {
		return u.isOnline();
	}

	@Override
	public boolean hasPlayedBefore() {
		return false;
	}

	@Override
	public boolean isOp() {
		return u.hasPermission("*");
	}

	@Override
	public String getName() {
		return u.getName();
	}

	@Override
	public Object getDefault() {
		return u;
	}
	
	
}
