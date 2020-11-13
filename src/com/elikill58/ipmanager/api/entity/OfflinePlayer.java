package com.elikill58.ipmanager.api.entity;

import java.util.UUID;

import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;

public abstract class OfflinePlayer extends Entity {

	public abstract UUID getUniqueId();
	
	public abstract boolean isOnline();
	
	public abstract boolean hasPlayedBefore();
	
	@Override
	public boolean isOnGround() {
		return true;
	}
	
	@Override
	public Location getLocation() {
		return null;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public Vector getRotation() {
		return null;
	}

	@Override
	public void sendMessage(String msg) {}
}
