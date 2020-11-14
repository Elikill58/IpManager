package com.elikill58.ipmanager.common;

import java.util.List;
import java.util.StringJoiner;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.EventListener;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.Listeners;
import com.elikill58.ipmanager.api.events.ipmanager.WrongProxyEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.utils.Utils;
import com.elikill58.ipmanager.api.yaml.config.Configuration;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Messages;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class ConnectionEvents implements Listeners {
	
	@EventListener
	public void onLogin(LoginEvent e) {
		Adapter pl = Adapter.getAdapter();
		Configuration config = pl.getConfig();
		String bungeeIP = e.getRealAddress().getHost(), basicIp = e.getAddress().getHost();
		if(config.getBoolean("log_console"))
			pl.getLogger().info(Messages.getMessage("messages.log_console", "%name%", e.getName(), "%uuid%", e.getUUID().toString(), "%ip%", basicIp));
		IpPlayerAccount ip = IpPlayerAccountManager.getManager().getNow(e.getUUID());
		ip.setBasicIp(basicIp);
		ip.setProxy(bungeeIP);
		ip.save();
		
		if(config.getBoolean("only_proxy.enabled")) {
			if(!config.getStringList("only_proxy.bungee_ip").contains(bungeeIP)) {
				if(config.getBoolean("only_proxy.kick")) {
					e.setKickMessage(Messages.getMessage("messages.wrong_proxy"));
					e.setLoginResult(Result.KICK_OTHER);
				}
				String cmd = config.getString("only_proxy.command_wrong_proxy");
				if(!cmd.isEmpty()) {
					Adapter.getAdapter().runConsoleCommand(cmd);
				}
				EventManager.callEvent(new WrongProxyEvent(ip, bungeeIP, e));
			}
		}

		if(config.getBoolean("banned-ip.enabled")) {
			if(config.getStringList("banned-ip.disallow").contains(basicIp)) {
				e.setKickMessage(Messages.getMessage("messages.ip_not_allowed"));
				e.setLoginResult(Result.KICK_BANNED);
			}
		}
	}
	
	private Configuration getConfigForAmountPlayer(Configuration configuration, int nb) {
		if(configuration == null)
			return null;
		Configuration sec = null;
		int tempI = -1;
		for(String key : configuration.getKeys()) {
			if(!UniversalUtils.isInteger(key) || !(configuration.get(key) instanceof Configuration))
				continue;
			int i = Integer.parseInt(key);
			if(i == nb)
				return configuration.getSection(key);
			else if(i < nb && i > tempI) {
				sec = configuration.getSection(key);
				tempI = i;
			}
		}
		return sec;
	}
	
	@EventListener
	public void onJoin(PlayerConnectEvent e) {
		Player p = e.getPlayer();
		IpPlayerAccount ip = IpPlayerAccountManager.getManager().getNow(p.getUniqueId());
		ip.setBasicIp(p.getIP().getHost());
		ip.loadIP();
		ip.setFai(p.getAddress().getHostName());
		ip.save();
		Adapter ada = Adapter.getAdapter();
		List<Player> playersOnIp = Players.getPlayersOnIP(ip.getBasicIp());
		Configuration playerSection = getConfigForAmountPlayer(ada.getConfig().getSection("ip-notify"), playersOnIp.size());
		if(playerSection == null)
			return;
		String perm = playerSection.getString("permission", "");
		StringJoiner allNames = new StringJoiner(", ");
		playersOnIp.forEach((pp) -> allNames.add(pp.getName()));
		playerSection.getStringList("actions").forEach((action) -> {
			if(!action.contains(":")) {
				Adapter.getAdapter().getLogger().warn("Wrong value for " + action + ".");
				return;
			}
			String value = Utils.coloredMessage(action.split(":", 2)[1].replaceAll("%name%", p.getName()).replaceAll("%uuid%", p.getUniqueId().toString())
					.replaceAll("%count%", String.valueOf(playersOnIp.size())).replaceAll("%all_names%", allNames.toString()));
			if(action.startsWith("console:")) {
				ada.runConsoleCommand(value);
			} else if(action.startsWith("run:")) {
				p.performCommand(value);
			} else if(action.startsWith("kick:")) {
				p.kick(value);
			} else if(action.startsWith("send:")) {
				ada.getOnlinePlayers().forEach((mod) -> {
					if((perm.isEmpty() && mod.isOp()) || mod.hasPermission(perm))
						mod.sendMessage(value);
				});
			}
		});
	}

}
