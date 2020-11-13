package com.elikill58.ipmanager.api.entity;

import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;

public abstract class Entity extends CommandSender {

	public abstract boolean isOnGround();
	public abstract boolean isOp();
	
	public abstract Location getLocation();
	
	public abstract Vector getRotation();
	
	public abstract EntityType getType();
	
	@Override
	public String toString() {
		return "Entity{type=" + getType().name() + ",x=" + getLocation().getX() + ",y=" + getLocation().getY() + ",z=" + getLocation().getZ() + "}";
	}
}
