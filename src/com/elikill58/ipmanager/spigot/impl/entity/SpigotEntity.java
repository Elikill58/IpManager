package com.elikill58.ipmanager.spigot.impl.entity;

import com.elikill58.ipmanager.api.entity.Entity;
import com.elikill58.ipmanager.api.entity.EntityType;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;
import com.elikill58.ipmanager.spigot.impl.location.SpigotLocation;

public class SpigotEntity extends Entity {

	private final org.bukkit.entity.Entity entity;
	private final SpigotLocation loc;
	
	public SpigotEntity(org.bukkit.entity.Entity entity) {
		this.entity = entity;
		this.loc = new SpigotLocation(entity.getLocation());
	}

	@Override
	public boolean isOnGround() {
		return entity.isOnGround();
	}

	@Override
	public boolean isOp() {
		return entity.isOp();
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public EntityType getType() {
		return EntityType.get(entity == null ? null : entity.getType().name());
	}

	@Override
	public Object getDefault() {
		return entity;
	}

	@Override
	public void sendMessage(String msg) {
		entity.sendMessage(msg);
	}

	@Override
	public String getName() {
		return entity.getName();
	}
	
	@Override
	public Vector getRotation() {
		org.bukkit.util.Vector vec = entity.getLocation().getDirection();
		return new Vector(vec.getX(), vec.getY(), vec.getZ());
	}
}
