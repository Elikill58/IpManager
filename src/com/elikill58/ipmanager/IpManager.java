package com.elikill58.ipmanager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class IpManager extends JavaPlugin {

	private static IpManager instance;
	public static IpManager getInstance() {
		return instance;
	}
	private YamlConfiguration ipConfig;
	private File ipFile;
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);
		saveDefaultConfig();
		getConfig().addDefault("log_console", "true");

		ipFile = new File(getDataFolder().getAbsolutePath() + File.separator + "users.yml");
		if(!ipFile.exists())
			Utils.copy(this, "users.yml", ipFile);
		ipConfig = YamlConfiguration.loadConfiguration(ipFile);
		
		getCommand("getip").setExecutor(new GetIpCommand());
	}
	
	public YamlConfiguration getIPConfig() {
		return ipConfig;
	}
	
	public void saveIPConfig() {
		try {
			ipConfig.save(ipFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
