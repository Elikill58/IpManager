package com.elikill58.ipmanager.velocity.impl.entity;

import java.net.InetSocketAddress;
import java.util.UUID;

import com.elikill58.ipmanager.api.GameMode;
import com.elikill58.ipmanager.api.entity.Entity;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.PlayerInventory;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;
import com.elikill58.ipmanager.api.location.World;

import net.kyori.text.TextComponent;

public class VelocityPlayer extends Player {

	private final com.velocitypowered.api.proxy.Player pp;
	
	public VelocityPlayer(com.velocitypowered.api.proxy.Player pp) {
		this.pp = pp;
	}

	@Override
	public UUID getUniqueId() {
		return pp.getUniqueId();
	}
	
	@Override
	public String getIP() {
		return pp.getRemoteAddress().getAddress().getHostAddress();
	}

	@Override
	public boolean isOnline() {
		return pp.isActive();
	}

	@Override
	public boolean hasPlayedBefore() {
		return true;
	}

	@Override
	public boolean isOp() {
		return pp.hasPermission("*");
	}

	@Override
	public void sendMessage(String msg) {
		pp.sendMessage(TextComponent.of(msg));
	}

	@Override
	public String getName() {
		return pp.getUsername();
	}

	@Override
	public Object getDefault() {
		return pp;
	}

	@Override
	public boolean hasPermission(String perm) {
		return pp.hasPermission(perm);
	}
	
	@Override
	public void kick(String reason) {
		pp.disconnect(TextComponent.of(reason));
	}

	@Override
	public int getPing() {
		return (int) pp.getPing();
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean getAllowFlight() {
		return false;
	}

	@Override
	public boolean isFlying() {
		return false;
	}

	@Override
	public void setAllowFlight(boolean b) {}

	@Override
	public GameMode getGameMode() {
		return null;
	}
	
	@Override
	public void setGameMode(GameMode gameMode) {}

	@Override
	public void damage(double amount) {}

	@Override
	public void teleport(Location loc) {}

	@Override
	public void teleport(Entity et) {}

	@Override
	public boolean isSneaking() {
		return false;
	}

	@Override
	public void setSneaking(boolean b) {}

	@Override
	public boolean isSprinting() {
		return false;
	}

	@Override
	public void setSprinting(boolean b) {}

	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public PlayerInventory getInventory() {
		return null;
	}

	@Override
	public Inventory getOpenInventory() {
		return null;
	}

	@Override
	public boolean hasOpenInventory() {
		return false;
	}

	@Override
	public void openInventory(Inventory inv) {}

	@Override
	public void closeInventory() {}

	@Override
	public void updateInventory() {}

	@Override
	public Vector getVelocity() { return null; }

	@Override
	public void setVelocity(Vector vel) {}
	
	@Override
	public Vector getRotation() {
		return null;
	}
	
	@Override
	public void performCommand(String cmd) {
		
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return pp.getRemoteAddress();
	}
}
