package com.elikill58.ipmanager.api.events.player;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;

public class PlayerLeaveEvent implements Event {

	private final Player p;
	private final IpPlayerAccount ip;
	private String quitMessage;
	
	public PlayerLeaveEvent(Player p, IpPlayerAccount ip, String quitMessage) {
		this.p = p;
		this.ip = ip;
		this.quitMessage = quitMessage;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public IpPlayerAccount getIpPlayer() {
		return ip;
	}
	
	public String getQuitMessage() {
		return quitMessage;
	}
	
	public void setQuitMessage(String quitMessage) {
		this.quitMessage = quitMessage;
	}
}
