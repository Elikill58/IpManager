package com.elikill58.ipmanager.api.events.player;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;

public class PlayerConnectEvent implements Event {

	private final Player p;
	private final IpPlayerAccount ip;
	private String joinMessage;
	
	public PlayerConnectEvent(Player p, IpPlayerAccount ip, String joinMessage) {
		this.p = p;
		this.ip = ip;
		this.joinMessage = joinMessage;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public IpPlayerAccount getIpPlayer() {
		return ip;
	}
	
	public String getJoinMessage() {
		return joinMessage;
	}
	
	public void setJoinMessage(String joinMessage) {
		this.joinMessage = joinMessage;
	}
}
