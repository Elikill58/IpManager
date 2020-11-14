package com.elikill58.ipmanager.common.commands;

import java.util.List;
import java.util.UUID;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.commands.CommandListeners;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.commands.TabListeners;
import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.spigot.SpigotIpManager;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.IP;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.Messages;
import com.elikill58.ipmanager.universal.IP.IpInfos;

public class GetIpCommand implements CommandListeners, TabListeners {

	@Override
	public boolean onCommand(CommandSender sender, String[] arg, String prefix) {
		if (arg.length == 0) {
			sender.sendMessage(Messages.getMessage("messages.precise"));
			return true;
		}
		if(arg[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(Messages.getMessage("messages.reloaded"));
			Adapter.getAdapter().reload();
			IpManager.init();
			return false;
		}
		OfflinePlayer of = Adapter.getAdapter().getOfflinePlayer(arg[0]);
		if (of == null) {
			try {
				of = Adapter.getAdapter().getOfflinePlayer(UUID.fromString(arg[0]));
			} catch (Exception e) {
			}
		}
		if (of == null || of.getUniqueId() == null) {
			sender.sendMessage(Messages.getMessage("messages.cannot_found"));
			return false;
		}
		// TODO fix
		if (SpigotIpManager.getInstance().getConfig().contains(of.getUniqueId().toString() + ".name")) {
			if (of.isOnline()) {
				IpPlayer pp = IpPlayer.getIpPlayer((Player) of);
				IP ip = pp.getIP();
				for (String s : Messages.getStringList("messages.getip.online", "%name%", of.getName(), "%uuid%",
						of.getUniqueId().toString(), "%ip%", get(pp.getBasicIP()), "%proxy_ip%", get(pp.getBungeeIP()),
						"%fai%", get(pp.getFaiIP()), "%vpn%", Messages.getMessage(ip.isVPN()), "%proxy%",
						Messages.getMessage(ip.isProxy()), "%hosting%", Messages.getMessage(ip.isHosting()))) {
					if (s == null)
						continue;
					for (IpInfos ii : IpInfos.values())
						s = s.replaceAll("%" + ii.name().toLowerCase() + "%", get(ip.getIpInfos(ii)));
					sender.sendMessage(s);
				}
			} else {
				IpPlayer pp = IpPlayer.getPlayer(of.getUniqueId(), () -> null);
				Messages.getStringList("messages.getip.offline", "%name%", of.getName(), "%uuid%",
						of.getUniqueId().toString(), "%ip%", get(pp.getBasicIP()), "%proxy_ip%", get(pp.getBungeeIP()),
						"%fai%", get(pp.getFaiIP())).forEach(sender::sendMessage);
			}
		} else
			sender.sendMessage(Messages.getMessage("messages.not_registered", "%name%", of.getName(), "%uuid%",
					of.getUniqueId().toString()));

		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] arg, String prefix) {
		return null;
	}

	private String get(String s) {
		return s == null ? Messages.getMessage("messages.unknow") : s;
	}
}
