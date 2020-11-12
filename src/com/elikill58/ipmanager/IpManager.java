package com.elikill58.ipmanager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.elikill58.ipmanager.handler.IpPlayer;
import com.elikill58.ipmanager.listeners.WrongProxyEvent;

public class IpManager extends JavaPlugin implements Listener {

	private static IpManager instance;
	public static IpManager getInstance() {
		return instance;
	}
	private YamlConfiguration ipConfig;
	private File ipFile;
	
	@Override
	public void onEnable() {
		instance = this;
		
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		getConfig().addDefault("log_console", "true");

		ipFile = new File(getDataFolder().getAbsolutePath() + File.separator + "users.yml");
		if(!ipFile.exists())
			Utils.copy(this, "users.yml", ipFile);
		ipConfig = YamlConfiguration.loadConfiguration(ipFile);
		
		getCommand("getip").setExecutor(new GetIpCommand());
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(getConfig().getBoolean("log_console"))
			getLogger().info(Messages.getMessage("messages.log_console", "%name%", p.getName(), "%uuid%", p.getUniqueId().toString(), "%ip%", e.getAddress().getHostAddress()));
		String bungeeIP = e.getRealAddress().getHostAddress(), basicIp = e.getAddress().getHostAddress();
		IpPlayer ip = IpPlayer.getIpPlayer(p);
		ip.setBasicIP(basicIp);
		ip.setBungeeIP(bungeeIP);
		
		if(getConfig().getBoolean("only_proxy.enabled")) {
			if(!getConfig().getStringList("only_proxy.bungee_ip").contains(bungeeIP)) {
				if(getConfig().getBoolean("only_proxy.kick")) {
					e.setKickMessage(Messages.getMessage("messages.wrong_proxy"));
					e.setResult(Result.KICK_OTHER);
				}
				String cmd = getConfig().getString("only_proxy.command_wrong_proxy");
				if(!cmd.isEmpty()) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
				Bukkit.getPluginManager().callEvent(new WrongProxyEvent(p, bungeeIP, e));
			}
		}

		if(getConfig().getBoolean("banned-ip.enabled")) {
			if(getConfig().getStringList("banned-ip.disallow").contains(basicIp)) {
				e.setKickMessage(Messages.getMessage("messages.ip_not_allowed"));
				e.setResult(Result.KICK_BANNED);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		IpPlayer ip = IpPlayer.getIpPlayer(p);
		ip.loadIP();
		ip.setFaiIP(p.getAddress().getHostName());
		ip.save();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		IpPlayer.getIpPlayer(e.getPlayer()).remove();
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
