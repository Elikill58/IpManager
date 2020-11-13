package com.elikill58.ipmanager.universal.account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contains player-related data that can be accessed when the player is offline.
 */
public final class IpManagerAccount {

	private final UUID playerId;
	private String playerName;
	private String ip, proxy, fai;
	private final List<Long> allConnections;
	private final long creationTime;

	public IpManagerAccount(UUID playerId) {
		this(playerId, null, null, null, null, new ArrayList<>(), System.currentTimeMillis());
	}

	public IpManagerAccount(UUID playerId, String playerName, String ip, String proxy, String fai, List<Long> connection, long creationTime) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.ip = ip;
		this.setProxy(proxy);
		this.setFai(fai);
		this.allConnections = connection;
		this.creationTime = creationTime;
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
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
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
}
