package com.elikill58.ipmanager.bungee;

import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeIpManager extends Plugin {

	@Override
	public void onEnable() {
		Adapter.setAdapter(new BungeeAdapter(this));
		
		IpManager.init();

		PluginManager pluginManager = getProxy().getPluginManager();
		pluginManager.registerListener(this, new BungeeListeners());
		BungeeGetIpCommand getIpCmd = new BungeeGetIpCommand();
		pluginManager.registerCommand(this, getIpCmd);
		pluginManager.registerListener(this, getIpCmd);

		IpManagerAccountStorage.setDefaultStorage("file");
	}

	@Override
	public void onDisable() {
		Database.close();
	}
}
