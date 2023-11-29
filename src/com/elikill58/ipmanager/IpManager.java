package com.elikill58.ipmanager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.elikill58.ipmanager.handler.IP;
import com.elikill58.ipmanager.handler.IpPlayer;

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
		getServer().getPluginManager().registerEvents(new ConnectionEvents(this), this);
		saveDefaultConfig();

		loadIpConfig();
		
		getCommand("getip").setExecutor(new GetIpCommand());
		
		CompletableFuture.runAsync(() -> {
			for(Player p : Bukkit.getOnlinePlayers()) {
				IpPlayer ip = IpPlayer.getIpPlayer(p);
				ip.setIp(IP.getIP(ip.getBasicIP()));
				ip.setFaiIP(p.getAddress().getHostName());
				ip.save();
			}
		});
	}
	
	private void loadIpConfig() {
		ipFile = new File(getDataFolder().getAbsolutePath(), "users.yml");
		if(!ipFile.exists())
			Utils.copy(this, "users.yml", ipFile);
		ipConfig = YamlConfiguration.loadConfiguration(ipFile);
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

	public void reload() {
		reloadConfig();
		
		loadIpConfig();
	}
}
