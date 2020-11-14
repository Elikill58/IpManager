package com.elikill58.ipmanager.universal.account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.universal.IP;

/**
 * Contains player-related data that can be accessed when the player is offline.
 */
public final class IpPlayerAccount {

	private IP ip = null;
	private final UUID playerId;
	private String playerName;
	private String basicIp, proxy, fai;
	private final List<Long> allConnections;
	private final long creationTime;

	public IpPlayerAccount(UUID playerId) {
		this(playerId, null, null, null, null, new ArrayList<>(), System.currentTimeMillis());
	}

	public IpPlayerAccount(UUID playerId, String playerName, String ip, String proxy, String fai, List<Long> connection, long creationTime) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.basicIp = ip == null && Players.getPlayer(playerId) != null ? Players.getPlayer(playerId).getIP() : ip;
		this.proxy = proxy;
		this.fai = fai;
		this.allConnections = connection;
		this.creationTime = creationTime;
		
		loadIP();
	}

	public UUID getPlayerId() {
		return playerId;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public String getBasicIp() {
		return basicIp;
	}
	
	public void setBasicIp(String ip) {
		this.basicIp = ip;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getFai() {
		return fai;
	}

	public void setFai(String fai) {
		this.fai = fai;
	}

	public List<Long> getAllConnections() {
		return allConnections;
	}
	
	public boolean isIP(String checkIp) {
		return basicIp.equalsIgnoreCase(checkIp);
	}
	
	public IP getIP() {
		if(ip == null)
			loadIP();
		return ip;
	}
	
	public void loadIP() {
		if(ip != null || basicIp == null) {
			return;
		}
		this.ip = IP.getIP(basicIp);
	}

	public void save() {
		IpPlayerAccountManager.getManager().save(playerId);
	}
}
