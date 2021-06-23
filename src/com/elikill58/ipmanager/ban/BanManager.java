package com.elikill58.ipmanager.ban;

import org.bukkit.configuration.ConfigurationSection;

import com.elikill58.ipmanager.IpManager;

public class BanManager {

	private static BanManager instance;
	public static BanManager getInstance() {
		if(instance == null)
			instance = new BanManager(IpManager.getInstance().getConfig().getConfigurationSection("ban"));
		return instance;
	}
	
	private final BanProcessor processor;
	private final ConfigurationSection config;
	
	public BanManager(ConfigurationSection config) {
		this.config = config;
		processor = BanProcessor.valueOf(config.getString("processor", "command").toUpperCase());
	}
	
	public BanProcessor getProcessor() {
		return processor;
	}

	public String getCommand() {
		return config.getString("command");
	}
}
