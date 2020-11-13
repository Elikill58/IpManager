package com.elikill58.ipmanager.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.yaml.config.Configuration;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.IP;
import com.elikill58.ipmanager.universal.IpManager;

public class IpPlayer {

	private static final HashMap<UUID, IpPlayer> IP_PLAYERS = new HashMap<>();
	
	private final UUID uuid;
	private Player p;
	private IP ip = null;
	private String basicIP, bungeeIP, faiIP;
	private final long connectionTime;
	
	public IpPlayer(UUID uuid) {
		this(uuid, null);
	}
	
	public IpPlayer(UUID uuid, Player p) {
		this.uuid = uuid;
		this.p = p;
		this.connectionTime = System.currentTimeMillis();
		Configuration conf = IpManager.getIPConfig();
		this.basicIP = conf.getString(uuid.toString() + ".ip");
		this.bungeeIP = conf.getString(uuid.toString() + ".proxy");
		this.faiIP = conf.getString(uuid.toString() + ".fai");
	}
	
	public Player getPlayer() {
		if(p == null)
			p = Adapter.getAdapter().getPlayer(uuid);
		return p;
	}
	
	public boolean isIP(String checkIp) {
		return basicIP.equalsIgnoreCase(checkIp);
	}
	
	public IP getIP() {
		if(ip == null)
			loadIP();
		return ip;
	}
	
	public void loadIP() {
		if(ip != null) {
			return;
		}
		this.ip = IP.getIP(basicIP);
	}
	
	public String getBasicIP() {
		return basicIP;
	}
	
	public void setBasicIP(String ip) {
		this.basicIP = ip;
	}
	
	public String getBungeeIP() {
		return bungeeIP;
	}
	
	public void setBungeeIP(String ip) {
		this.bungeeIP = ip;
	}
	
	public String getFaiIP() {
		return faiIP;
	}
	
	public void setFaiIP(String ip) {
		this.faiIP = ip;
	}
	
	public long getConnectionTime() {
		return connectionTime;
	}
	
	public long getOnlineTime() {
		return System.currentTimeMillis() - connectionTime;
	}

	public void remove() {
		save();
	}
	
	public void save() {
		Configuration conf = IpManager.getIPConfig();
		String uuid = p.getUniqueId().toString();
		conf.set(uuid + ".name", p.getName());
		conf.set(uuid + ".ip", basicIP);
		conf.set(uuid + ".proxy", bungeeIP);
		conf.set(uuid + ".fai", faiIP);
		List<Long> list = conf.getLongList(uuid + ".connection");
		list.add(connectionTime);
		conf.set(uuid + ".connection", list);
		IpManager.saveIPConfig();
	}
	
	public static List<IpPlayer> getPlayersOnIP(String ip){
		return IP_PLAYERS.values().stream().filter((ipp) -> ipp.isIP(ip)).collect(Collectors.toList());
	}
	
	public static IpPlayer getIpPlayer(Player p) {
		return IP_PLAYERS.computeIfAbsent(p.getUniqueId(), (uuid) -> new IpPlayer(uuid, p));
	}

	
	/**
	 * Get the Negativity Player or create a new one
	 * 
	 * @param p the player which we are looking for it's NegativityPlayer
	 * @return the negativity player
	 */
	public static IpPlayer getPlayer(Player p) {
		return IP_PLAYERS.computeIfAbsent(p.getUniqueId(), uuid -> new IpPlayer(uuid, p));
	}

	/**
	 * Get the Negativity Player or create a new one
	 * 
	 * @param uuid the player UUID
	 * @param call a creator of a new player
	 * @return the negativity player
	 */
	public static IpPlayer getPlayer(UUID uuid, Callable<Player> call) {
		return IP_PLAYERS.computeIfAbsent(uuid, id -> {
			try {
				return new IpPlayer(uuid, call.call());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}
	
	/**
	 * Get all uuid and their negativity players
	 * 
	 * @return negativity players
	 */
	public static Map<UUID, IpPlayer> getAllPlayers(){
		return IP_PLAYERS;
	}

	/**
	 * Remove the player from cache
	 * 
	 * @param playerId the player UUID
	 */
	public static void removeFromCache(UUID playerId) {
		IpPlayer cached = IP_PLAYERS.remove(playerId);
		if (cached != null) {
			cached.remove();
		}
	}
}
