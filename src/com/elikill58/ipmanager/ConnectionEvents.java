package com.elikill58.ipmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.elikill58.ipmanager.handler.IpPlayer;
import com.elikill58.ipmanager.listeners.WrongProxyEvent;

public class ConnectionEvents implements Listener {

	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		IpManager pl = IpManager.getInstance();
		ConfigurationSection config = pl.getConfig();
		if(config.getBoolean("log_console"))
			pl.getLogger().info(Messages.getMessage("messages.log_console", "%name%", p.getName(), "%uuid%", p.getUniqueId().toString(), "%ip%", e.getAddress().getHostAddress()));
		String bungeeIP = e.getRealAddress().getHostAddress(), basicIp = e.getAddress().getHostAddress();
		IpPlayer ip = IpPlayer.getIpPlayer(p);
		ip.setBasicIP(basicIp);
		ip.setBungeeIP(bungeeIP);
		
		if(config.getBoolean("only_proxy.enabled")) {
			if(!config.getStringList("only_proxy.bungee_ip").contains(bungeeIP)) {
				if(config.getBoolean("only_proxy.kick")) {
					e.setKickMessage(Messages.getMessage("messages.wrong_proxy"));
					e.setResult(Result.KICK_OTHER);
				}
				String cmd = config.getString("only_proxy.command_wrong_proxy");
				if(!cmd.isEmpty()) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
				Bukkit.getPluginManager().callEvent(new WrongProxyEvent(p, bungeeIP, e));
			}
		}

		if(config.getBoolean("banned-ip.enabled")) {
			if(config.getStringList("banned-ip.disallow").contains(basicIp)) {
				e.setKickMessage(Messages.getMessage("messages.ip_not_allowed"));
				e.setResult(Result.KICK_BANNED);
			}
		}
		if(!e.getResult().equals(Result.ALLOWED))
			return; // only if the player will stay online
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		IpPlayer ip = IpPlayer.getIpPlayer(p);
		ip.loadIP();
		ip.setFaiIP(p.getAddress().getHostName());
		ip.save();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		IpPlayer.getIpPlayer(e.getPlayer()).remove();
	}
}
