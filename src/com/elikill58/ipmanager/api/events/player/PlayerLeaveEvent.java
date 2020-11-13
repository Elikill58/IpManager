package com.elikill58.ipmanager.api.events.player;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;

public class PlayerLeaveEvent implements Event {

	private final Player p;
	private final IpPlayer ip;
	private String quitMessage;
	
	public PlayerLeaveEvent(Player p, IpPlayer ip, String quitMessage) {
		this.p = p;
		this.ip = ip;
		this.quitMessage = quitMessage;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public IpPlayer getIpPlayer() {
		return ip;
	}
	
	public String getQuitMessage() {
		return quitMessage;
	}
	
	public void setQuitMessage(String quitMessage) {
		this.quitMessage = quitMessage;
	}
}
