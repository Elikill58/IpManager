package com.elikill58.ipmanager.api.events.player;

import java.util.UUID;

import com.elikill58.ipmanager.api.PlayerAddress;
import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;

public class LoginEvent implements Event {

	private final IpPlayerAccount ip;
	private final UUID uuid;
	private final String name;
	private final PlayerAddress address, realAddress;
	private Result result;
	private String kickMessage;
	
	public LoginEvent(IpPlayerAccount ip, UUID uuid, String name, Result result, PlayerAddress address, PlayerAddress realAddress, String kickMessage) {
		this.ip = ip;
		this.uuid = uuid;
		this.name = name;
		this.result = result;
		this.address = address;
		this.realAddress = realAddress;
		this.kickMessage = kickMessage;
	}
	
	public IpPlayerAccount getIpPlayer() {
		return ip;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public Result getLoginResult() {
		return result;
	}
	
	public PlayerAddress getAddress() {
		return address;
	}
	
	public PlayerAddress getRealAddress() {
		return realAddress;
	}
	
	public void setLoginResult(Result result) {
		this.result = result;
	}
	
	public String getKickMessage() {
		return kickMessage;
	}
	
	public void setKickMessage(String kickMessage) {
		this.kickMessage = kickMessage;
	}
	
	public static enum Result {
		  ALLOWED, KICK_FULL, KICK_BANNED, KICK_WHITELIST, KICK_OTHER;
	}
}
