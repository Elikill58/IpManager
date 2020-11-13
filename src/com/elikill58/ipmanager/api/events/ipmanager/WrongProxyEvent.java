package com.elikill58.ipmanager.api.events.ipmanager;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.api.events.player.LoginEvent;

public class WrongProxyEvent implements Event {

	private final IpPlayer p;
	private final String proxyIP;
	private final LoginEvent event;
	
	public WrongProxyEvent(IpPlayer p, String proxyIP, LoginEvent event) {
		this.p = p;
		this.proxyIP = proxyIP;
		this.event = event;
	}
	
	public IpPlayer getIpPlayer() {
		return p;
	}
	
	public String getProxyIP() {
		return proxyIP;
	}
	
	public LoginEvent getEvent() {
		return event;
	}
}
