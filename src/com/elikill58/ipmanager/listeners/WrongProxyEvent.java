package com.elikill58.ipmanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLoginEvent;

public class WrongProxyEvent extends Event {

	private final Player p;
	private final String proxyIP;
	private final PlayerLoginEvent event;
	
	public WrongProxyEvent(Player p, String proxyIP, PlayerLoginEvent event) {
		this.p = p;
		this.proxyIP = proxyIP;
		this.event = event;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public String getProxyIP() {
		return proxyIP;
	}
	
	public PlayerLoginEvent getEvent() {
		return event;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
