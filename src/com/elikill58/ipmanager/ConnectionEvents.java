package com.elikill58.ipmanager;

import java.util.List;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elikill58.ipmanager.handler.IpPlayer;
import com.elikill58.ipmanager.listeners.WrongProxyEvent;

public class ConnectionEvents implements Listener {

	private final IpManager pl;
	
	public ConnectionEvents(IpManager pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
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
	}
	
	private ConfigurationSection getConfigForAmountPlayer(ConfigurationSection config, int nb) {
		if(config == null)
			return null;
		ConfigurationSection sec = null;
		int tempI = -1;
		for(String key : config.getKeys(false)) {
			if(!Utils.isInteger(key) || !(config.get(key) instanceof ConfigurationSection))
				continue;
			int i = Integer.parseInt(key);
			if(i == nb)
				return config.getConfigurationSection(key);
			else if(i < nb && i > tempI) {
				sec = config.getConfigurationSection(key);
				tempI = i;
			}
		}
		return sec;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		IpPlayer ip = IpPlayer.getIpPlayer(p);
		ip.loadIP();
		ip.setFaiIP(p.getAddress().getHostName());
		ip.save();
		List<IpPlayer> playersOnIp = IpPlayer.getPlayersOnIP(ip.getBasicIP());
		ConfigurationSection playerSection = getConfigForAmountPlayer(pl.getConfig().getConfigurationSection("ip-notify"), playersOnIp.size());
		if(playerSection == null)
			return;
		String perm = playerSection.getString("permission", "");
		StringJoiner allNames = new StringJoiner(", ");
		playersOnIp.forEach((pp) -> allNames.add(pp.getPlayer().getName()));
		playerSection.getStringList("actions").forEach((action) -> {
			if(!action.contains(":")) {
				IpManager.getInstance().getLogger().warning("Wrong value for " + action + ", with path " + playerSection.getCurrentPath());
				return;
			}
			String value = Utils.applyColorCodes(action.split(":", 2)[1].replaceAll("%name%", p.getName()).replaceAll("%uuid%", p.getUniqueId().toString())
					.replaceAll("%count%", String.valueOf(playersOnIp.size())).replaceAll("%all_names%", allNames.toString()));
			if(action.startsWith("console:")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
			} else if(action.startsWith("run:")) {
				p.performCommand(value);
			} else if(action.startsWith("kick:")) {
				p.kickPlayer(value);
			} else if(action.startsWith("send:")) {
				Utils.getOnlinePlayers().forEach((mod) -> {
					if((perm.isEmpty() && mod.isOp()) || mod.hasPermission(perm))
						mod.sendMessage(value);
				});
			}
		});
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		IpPlayer.getIpPlayer(e.getPlayer()).remove();
	}
}
