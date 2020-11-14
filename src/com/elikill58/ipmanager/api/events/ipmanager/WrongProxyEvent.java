package com.elikill58.ipmanager.api.events.ipmanager;

import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;

public class WrongProxyEvent implements Event {

	private final IpPlayerAccount p;
	private final String proxyIP;
	private final LoginEvent event;
	
	public WrongProxyEvent(IpPlayerAccount p, String proxyIP, LoginEvent event) {
		this.p = p;
		this.proxyIP = proxyIP;
		this.event = event;
	}
	
	public IpPlayerAccount getIpPlayer() {
		return p;
	}
	
	public String getProxyIP() {
		return proxyIP;
	}
	
	public LoginEvent getEvent() {
		return event;
	}
}
