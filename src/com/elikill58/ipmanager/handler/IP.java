package com.elikill58.ipmanager.handler;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.elikill58.ipmanager.IpManager;
import com.elikill58.ipmanager.Utils;
import com.elikill58.ipmanager.exception.NotLoadedException;

public class IP {

	private static final HashMap<String, IP> IPS = new HashMap<>();
	private final String ip;
	private String allIpJsonInfos, asn = null, asnName = null;
	private HashMap<IpInfos, String> ipInfos = new HashMap<>();
	private boolean isVPN = false, isProxy = false, isHosting = false;
	
	public IP(String ip) {
		this.ip = ip;
		
		if(Bukkit.isPrimaryThread()) {
			IpManager pl = IpManager.getInstance();
			pl.getLogger().severe("Cannot load IP " + ip + " sync ... Loading it async but few error can appear.");
			CompletableFuture.runAsync(this::loadContent);
		} else
			loadContent();
	}

	private void loadContent() {
		String checkingVpn = null;
		try {
			checkingVpn = Utils.getContentFromUrl("https://api.negativity.fr/ip/" + ip);
			Object data = new JSONParser().parse(checkingVpn);
			if (data instanceof JSONObject) {
				JSONObject result = (JSONObject) data;
				isVPN = result.get("vpn") == "true";
				isProxy = result.get("proxy") == "true";
				isHosting = result.get("hosting") == "true";
				asn = result.get("code").toString();
				asnName = result.get("name").toString();
			} else
				throw new NoSuchFieldException("Cannot found JSON vpn data for '" + allIpJsonInfos + "' string.");
		} catch (Exception e) {
			IpManager.getInstance().getLogger().severe("Failed to load content from API, result: " + checkingVpn);
			e.printStackTrace();
		}
		allIpJsonInfos = Utils.getContentFromUrl("https://ipapi.co/" + ip + "/json/");
		try {
			Object data = new JSONParser().parse(allIpJsonInfos);
			if (data instanceof JSONObject) {
				JSONObject json = (JSONObject) data;
				for (IpInfos ii : IpInfos.values())
					if (!ii.equals(IpInfos.UNSET))
						ipInfos.put(ii, json.getOrDefault(ii.name().toLowerCase(), "unknow").toString());
			} else
				throw new NoSuchFieldException("Cannot found JSON data for '" + allIpJsonInfos + "' string.");
		} catch (Exception e) {
			ipInfos.put(IpInfos.UNSET, "Error while getting IP information : " + e.getMessage() + ".");
		}
	}
	
	public String getStringIP() {
		return ip;
	}

	public HashMap<IpInfos, String> getAllIpInfos() {
		return ipInfos;
	}

	public boolean isVPN() {
		return isVPN;
	}

	public boolean isProxy() {
		return isProxy;
	}

	public boolean isHosting() {
		return isHosting;
	}
	
	public String getASN() {
		return asn;
	}
	
	public String getASNName() {
		return asnName;
	}

	public String getIpInfos(IpInfos i) {
		if (ipInfos.isEmpty()) {
			try {
				throw new NotLoadedException("IP informations are not currently loaded. Please wait ...");
			} catch (NotLoadedException e) {
				e.printStackTrace();
			}
		}
		return ipInfos.getOrDefault(i, "");
	}

	public static IP getIP(String ip) {
		return IPS.computeIfAbsent(ip, (ipp) -> new IP(ipp));
	}

	public static enum IpInfos {
		CITY, REGION, REGION_CODE, COUNTRY_CODE, COUNTRY_NAME, CONTINENT_CODE, IN_EU, TIMEZONE, LANGUAGUES, ORG, UNSET;
	}
}
