package com.elikill58.ipmanager.api.entity;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;

import com.elikill58.ipmanager.api.GameMode;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.PlayerInventory;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.Vector;
import com.elikill58.ipmanager.api.location.World;

public abstract class Player extends OfflinePlayer {

	/**
	 * Get the player IP
	 * 
	 * @return player IP
	 */
	@Nullable
	public abstract String getIP();
	
	/**
	 * Know if the player is dead
	 * 
	 * @return true if the player is dead
	 */
	public abstract boolean isDead();
	/**
	 * Check if the player has the specified permission
	 * 
	 * @param perm the needed permission
	 * @return true if the player has permission
	 */
	public abstract boolean hasPermission(String perm);
	
	/**
	 * Check if the player is flying
	 * 
	 * @return true is the player fly
	 */
	public abstract boolean isFlying();
	/**
	 * Check if the player is authorized to fly
	 * 
	 * @return true if the player can fly
	 */
	public abstract boolean getAllowFlight();
	/**
	 * Edit the authorization to fly
	 * 
	 * @param b true if the player is allowed to fly
	 */
	public abstract void setAllowFlight(boolean b);

	/**
	 * Get current player latency
	 * 
	 * @return the player ping
	 */
	public abstract int getPing();
	
	/**
	 * Get player gamemode
	 * 
	 * @return the Gamemode
	 */
	public abstract GameMode getGameMode();
	
	/**
	 * Set the player gamemode
	 * Warn: support only default gamemode. Not modded server.
	 * 
	 * @param gameMode the new player gamemode
	 */
	public abstract void setGameMode(GameMode gameMode);

	/**
	 * Damage player according to damage amount
	 * 
	 * @param amount the quantity of damage
	 */
	public abstract void damage(double amount);
	/**
	 * Kick player with the specified reason
	 * 
	 * @param reason the reason of kick
	 */
	public abstract void kick(String reason);
	/**
	 * Teleport player to specified location
	 * 
	 * @param loc location destination
	 */
	public abstract void teleport(Location loc);
	/**
	 * Teleport player to specified entity
	 * 
	 * @param et entity destination
	 */
	public abstract void teleport(Entity et);

	public abstract boolean isSneaking();
	public abstract void setSneaking(boolean b);
	
	public abstract boolean isSprinting();
	public abstract void setSprinting(boolean b);

	/**
	 * Get player world
	 * 
	 * @return the world where the player is
	 */
	public abstract World getWorld();

	public abstract PlayerInventory getInventory();
	public abstract Inventory getOpenInventory();
	public abstract boolean hasOpenInventory();
	public abstract void openInventory(Inventory inv);
	public abstract void closeInventory();
	public abstract void updateInventory();
	
	/**
	 * Get current player velocity
	 * 
	 * @return the player velocity
	 */
	public abstract Vector getVelocity();
	/**
	 * Edit the player velocity
	 * 
	 * @param vel the new velocity
	 */
	public abstract void setVelocity(Vector vel);
	
	public abstract InetSocketAddress getAddress();
	
	/**
	 * Check if it's a new player
	 * 
	 * @return true if the player has already played
	 */
	@Override
	public boolean hasPlayedBefore() {
		return true;
	}
	
	public abstract void performCommand(String cmd);
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Player)) {
			return false;
		}
		return this.getUniqueId().equals(((Player) obj).getUniqueId());
	}

}
