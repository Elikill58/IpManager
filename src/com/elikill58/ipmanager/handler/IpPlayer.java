package com.elikill58.ipmanager.handler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;

import com.elikill58.ipmanager.IpManager;

public class IpPlayer {

	private static final HashMap<UUID, IpPlayer> IP_PLAYERS = new HashMap<>();
	
	private final OfflinePlayer p;
	private String basicIP, bungeeIP, faiIP;
	private final long connectionTime;
	
	public IpPlayer(OfflinePlayer p) {
		this.p = p;
		this.connectionTime = System.currentTimeMillis();
		IpManager im = IpManager.getInstance();
		String uuid = p.getUniqueId().toString();
		this.basicIP = im.getIPConfig().getString(uuid + ".ip");
		this.bungeeIP = im.getIPConfig().getString(uuid + ".proxy");
		this.faiIP = im.getIPConfig().getString(uuid + ".fai");
	}
	
	public OfflinePlayer getPlayer() {
		return p;
	}
	
	public boolean isIP(String checkIp) {
		return basicIP.equalsIgnoreCase(checkIp);
	}
	
	public IP getIP() {
		return IP.getIP(basicIP);
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
		IP_PLAYERS.remove(getPlayer().getUniqueId());
	}
	
	public void save() {
		IpManager im = IpManager.getInstance();
		String uuid = p.getUniqueId().toString();
		im.getIPConfig().set(uuid + ".name", p.getName());
		im.getIPConfig().set(uuid + ".ip", basicIP);
		im.getIPConfig().set(uuid + ".proxy", bungeeIP);
		im.getIPConfig().set(uuid + ".fai", faiIP);
		List<Long> list = im.getIPConfig().getLongList(uuid + ".connection");
		list.add(connectionTime);
		im.getIPConfig().set(uuid + ".connection", list);
		im.saveIPConfig();
	}
	
	public static List<IpPlayer> getPlayersOnIP(String ip){
		return IP_PLAYERS.values().stream().filter((ipp) -> ipp.isIP(ip)).collect(Collectors.toList());
	}
	
	public static IpPlayer getIpPlayer(OfflinePlayer p) {
		return IP_PLAYERS.computeIfAbsent(p.getUniqueId(), (uuid) -> new IpPlayer(p));
	}
}
