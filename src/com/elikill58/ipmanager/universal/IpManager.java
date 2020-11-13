package com.elikill58.ipmanager.universal;

import java.io.File;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.yaml.config.Configuration;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class IpManager {
	
	private static Configuration ipConfig;
	
	public static void init() {
		EventManager.load();
		Database.init();
		IpManagerAccountStorage.init();
		UniversalUtils.init();
		
		ipConfig = UniversalUtils.loadConfig(new File(Adapter.getAdapter().getDataFolder(), "users"), "users.yml");
	}
	
	public static void saveIPConfig() {
		ipConfig.save();
	}
	
	public static Configuration getIPConfig() {
		return ipConfig;
	}
}
