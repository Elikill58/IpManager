package com.elikill58.ipmanager.spigot.impl.entity;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;

import com.elikill58.ipmanager.api.GameMode;
import com.elikill58.ipmanager.api.entity.Entity;
import com.elikill58.ipmanager.api.entity.EntityType;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.PlayerInventory;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;
import com.elikill58.ipmanager.api.location.World;
import com.elikill58.ipmanager.spigot.SpigotIpManager;
import com.elikill58.ipmanager.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ipmanager.spigot.impl.inventory.SpigotPlayerInventory;
import com.elikill58.ipmanager.spigot.impl.location.SpigotLocation;
import com.elikill58.ipmanager.spigot.impl.location.SpigotWorld;
import com.elikill58.ipmanager.spigot.utils.PacketUtils;

public class SpigotPlayer extends Player {

	private final org.bukkit.entity.Player p;
	
	public SpigotPlayer(org.bukkit.entity.Player p) {
		this.p = p;
	}

	@Override
	public UUID getUniqueId() {
		return p.getUniqueId();
	}

	@Override
	public void sendMessage(String msg) {
		p.sendMessage(msg);
	}

	@Override
	public boolean isOnGround() {
		return ((org.bukkit.entity.Entity) p).isOnGround();
	}

	@Override
	public boolean isOp() {
		return p.isOp();
	}

	@Override
	public GameMode getGameMode() {
		return GameMode.get(p.getGameMode().name());
	}
	
	@Override
	public void setGameMode(GameMode gameMode) {
		p.setGameMode(org.bukkit.GameMode.valueOf(gameMode.name()));
	}

	@Override
	public void damage(double amount) {
		p.damage(amount);
	}

	@Override
	public Location getLocation() {
		return new SpigotLocation(p.getLocation());
	}

	@Override
	public int getPing() {
		try {
			Object entityPlayer = PacketUtils.getEntityPlayer(p);
			return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public World getWorld() {
		return new SpigotWorld(p.getWorld());
	}

	@Override
	public String getName() {
		return p.getName();
	}

	@Override
	public boolean hasPermission(String perm) {
		return p.hasPermission(perm);
	}

	@Override
	public void kick(String reason) {
		p.kickPlayer(reason);
	}

	@Override
	public boolean getAllowFlight() {
		return p.getAllowFlight();
	}

	@Override
	public boolean isFlying() {
		return p.isFlying();
	}

	@Override
	public boolean isSneaking() {
		return p.isSneaking();
	}

	@Override
	public String getIP() {
		return p.getAddress().getAddress().getHostAddress();
	}

	@Override
	public boolean isOnline() {
		return p.isOnline();
	}

	@Override
	public void setSneaking(boolean b) {
		p.setSneaking(b);
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public boolean isSprinting() {
		return p.isSprinting();
	}

	@Override
	public void teleport(Entity et) {
		teleport(et.getLocation());
	}

	@Override
	public void teleport(Location loc) {
		Bukkit.getScheduler().runTask(SpigotIpManager.getInstance(), () -> p.teleport((org.bukkit.Location) loc.getDefault()));
	}

	@Override
	public void setSprinting(boolean b) {
		p.setSprinting(b);
	}

	@Override
	public boolean isDead() {
		return p.getHealth() <= 0;
	}

	@Override
	public Vector getVelocity() {
		org.bukkit.util.Vector vel = p.getVelocity();
		return new Vector(vel.getX(), vel.getY(), vel.getZ());
	}

	@Override
	public PlayerInventory getInventory() {
		return new SpigotPlayerInventory(p.getInventory());
	}
	
	@Override
	public boolean hasOpenInventory() {
		return p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().getType().equals(InventoryType.CHEST);
	}

	@Override
	public Inventory getOpenInventory() {
		return p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null ? null
				: new SpigotInventory(p.getOpenInventory().getTopInventory());
	}

	@Override
	public void openInventory(Inventory inv) {
		Bukkit.getScheduler().runTask(SpigotIpManager.getInstance(), () -> p.openInventory((org.bukkit.inventory.Inventory) inv.getDefault()));
	}

	@Override
	public void closeInventory() {
		Bukkit.getScheduler().runTask(SpigotIpManager.getInstance(), () -> p.closeInventory());
	}

	@Override
	public void updateInventory() {
		Bukkit.getScheduler().runTask(SpigotIpManager.getInstance(), () -> p.updateInventory());
	}

	@Override
	public void setAllowFlight(boolean b) {
		p.setAllowFlight(b);
	}

	@Override
	public void setVelocity(Vector vel) {
		p.setVelocity(new org.bukkit.util.Vector(vel.getX(), vel.getY(), vel.getZ()));
	}

	@Override
	public Object getDefault() {
		return p;
	}
	
	@Override
	public Vector getRotation() {
		org.bukkit.util.Vector vec = p.getLocation().getDirection();
		return new Vector(vec.getX(), vec.getY(), vec.getZ());
	}
	
	@Override
	public void performCommand(String cmd) {
		p.performCommand(cmd);
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return p.getAddress();
	}
}
