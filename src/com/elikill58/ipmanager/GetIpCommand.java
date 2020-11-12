package com.elikill58.ipmanager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elikill58.ipmanager.handler.IP;
import com.elikill58.ipmanager.handler.IpOfflinePlayer;
import com.elikill58.ipmanager.handler.IpPlayer;
import com.elikill58.ipmanager.handler.IP.IpInfos;

@SuppressWarnings("deprecation")
public class GetIpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if (arg.length == 0) {
			Messages.sendMessage(sender, "messages.precise");
			return true;
		}
		OfflinePlayer of = Bukkit.getOfflinePlayer(arg[0]);
		if (of == null) {
			try {
				of = Bukkit.getOfflinePlayer(UUID.fromString(arg[0]));
			} catch (Exception e) {
			}
		}
		if (of == null || of.getUniqueId() == null) {
			Messages.sendMessage(sender, "messages.cannot_found");
			return false;
		}
		if (IpManager.getInstance().getIPConfig().contains(of.getUniqueId().toString() + ".name")) {
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
				IpOfflinePlayer pp = IpOfflinePlayer.getIpPlayer(of);
				Messages.sendMessageList(sender, "messages.getip.offline", "%name%", of.getName(), "%uuid%",
						of.getUniqueId().toString(), "%ip%", get(pp.getBasicIP()), "%proxy_ip%", get(pp.getBungeeIP()),
						"%fai%", get(pp.getFaiIP()));
			}
		} else
			Messages.sendMessage(sender, "messages.not_registered", "%name%", of.getName(), "%uuid%",
					of.getUniqueId().toString());

		return false;
	}

	private String get(String s) {
		return s == null ? Messages.getMessage("messages.unknow") : s;
	}
}
