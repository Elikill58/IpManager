package com.elikill58.ipmanager.universal;

import java.util.HashMap;

import com.elikill58.ipmanager.api.json.JSONObject;
import com.elikill58.ipmanager.api.json.parser.JSONParser;
import com.elikill58.ipmanager.universal.exception.NotLoadedException;
import com.elikill58.ipmanager.universal.logger.LoggerAdapter;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

@SuppressWarnings("unchecked")
public class IP {

	private static final HashMap<String, IP> IPS = new HashMap<>();
	private final String ip;
	private String allIpJsonInfos;
	private HashMap<IpInfos, String> ipInfos = new HashMap<>();
	private boolean isVPN = false, isProxy = false, isHosting = false;

	public IP(String ip) {
		this.ip = ip;
		if(ip.equalsIgnoreCase("127.0.0.1") || ip.equalsIgnoreCase("localhost")) {
			ipInfos.put(IpInfos.CITY, "All");
			ipInfos.put(IpInfos.REGION, "All");
			ipInfos.put(IpInfos.REGION_CODE, "--");
			ipInfos.put(IpInfos.COUNTRY_CODE, "All");
			ipInfos.put(IpInfos.COUNTRY_NAME, "All");
			ipInfos.put(IpInfos.CONTINENT_CODE, "");
			ipInfos.put(IpInfos.IN_EU, "Everywhere");
			ipInfos.put(IpInfos.TIMEZONE, "GMT");
			ipInfos.put(IpInfos.LANGUAGUES, "ALL");
			ipInfos.put(IpInfos.ASN, "Own");
			ipInfos.put(IpInfos.ORG, "Own");
			ipInfos.put(IpInfos.UNSET, "-");
		} else {
			new Thread(() -> {
				try {
					String checkingVpn = UniversalUtils
							.getContentFromURL(UniversalUtils.getServerURL() + "ipmanager.php?ip=" + ip).orElse("{}");
					Object data = new JSONParser().parse(checkingVpn);
					if (data instanceof JSONObject) {
						JSONObject json = (JSONObject) data;
						Object status = json.get("status");
						if (status.toString().equalsIgnoreCase("ok")) {
							JSONObject result = ((JSONObject) json.get("result"));
							isVPN = result.get("vpn") == "true";
							isProxy = result.get("proxy") == "true";
							isHosting = result.get("hosting") == "true";
						} else {
							LoggerAdapter log = Adapter.getAdapter().getLogger();
							log.error("Error while loading VPN data for " + ip + ": " + status.toString());
							log.error("Result: " + checkingVpn);
						}
					} else
						throw new NoSuchFieldException("Cannot found JSON vpn data for '" + allIpJsonInfos + "' string.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			new Thread(() -> allIpJsonInfos = UniversalUtils.getContentFromURL("https://ipapi.co/" + ip + "/json/")
					.orElse("{}")).start();
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
		CITY, REGION, REGION_CODE, COUNTRY_CODE, COUNTRY_NAME, CONTINENT_CODE, IN_EU, TIMEZONE, LANGUAGUES, ASN, ORG, UNSET;
	}
}
