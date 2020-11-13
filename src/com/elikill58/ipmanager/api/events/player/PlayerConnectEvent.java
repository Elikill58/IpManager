package com.elikill58.ipmanager.api.events.player;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;

public class PlayerConnectEvent implements Event {

	private final Player p;
	private final IpPlayer ip;
	private String joinMessage;
	
	public PlayerConnectEvent(Player p, IpPlayer ip, String joinMessage) {
		this.p = p;
		this.ip = ip;
		this.joinMessage = joinMessage;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public IpPlayer getIpPlayer() {
		return ip;
	}
	
	public String getJoinMessage() {
		return joinMessage;
	}
	
	public void setJoinMessage(String joinMessage) {
		this.joinMessage = joinMessage;
	}
}
