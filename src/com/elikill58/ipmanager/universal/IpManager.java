package com.elikill58.ipmanager.universal;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class IpManager {
	
	public static void init() {
		EventManager.load();
		Database.init();
		IpManagerAccountStorage.init();
		UniversalUtils.init();
	}
}
