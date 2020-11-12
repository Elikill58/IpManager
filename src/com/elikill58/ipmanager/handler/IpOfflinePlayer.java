package com.elikill58.ipmanager.handler;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.elikill58.ipmanager.IpManager;

public class IpOfflinePlayer extends IpPlayerAbstract {

	private static final HashMap<UUID, IpOfflinePlayer> IP_PLAYERS = new HashMap<>();
	
	private final OfflinePlayer p;
	private final String basicIP, bungeeIP, faiIP;
	
	public IpOfflinePlayer(OfflinePlayer p) {
		this.p = p;
		IpManager im = IpManager.getInstance();
		String uuid = p.getUniqueId().toString();
		this.basicIP = im.getIPConfig().getString(uuid + ".ip");
		this.bungeeIP = im.getIPConfig().getString(uuid + ".proxy");
		this.faiIP = im.getIPConfig().getString(uuid + ".fai");
		
		IP_PLAYERS.put(p.getUniqueId(), this);
	}
	
	public OfflinePlayer getPlayer() {
		return p;
	}
	
	public String getBasicIP() {
		return basicIP;
	}
	
	public String getBungeeIP() {
		return bungeeIP;
	}
	
	public String getFaiIP() {
		return faiIP;
	}
	
	public static IpOfflinePlayer getIpPlayer(OfflinePlayer p) {
		if(IP_PLAYERS.containsKey(p.getUniqueId()))
			return IP_PLAYERS.get(p.getUniqueId());
		return new IpOfflinePlayer(p);
	}
	
}
