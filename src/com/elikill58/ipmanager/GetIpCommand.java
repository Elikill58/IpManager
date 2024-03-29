package com.elikill58.ipmanager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.elikill58.ipmanager.handler.IP;
import com.elikill58.ipmanager.handler.IP.IpInfos;
import com.elikill58.ipmanager.handler.IpPlayer;

@SuppressWarnings("deprecation")
public class GetIpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if (arg.length == 0) {
			Messages.sendMessage(sender, "messages.precise");
			return true;
		}
		if (arg[0].equalsIgnoreCase("reload")) {
			Messages.sendMessage(sender, "messages.reloaded");
			IpManager.getInstance().reload();
			return false;
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
			OfflinePlayer cible = of;
			CompletableFuture.runAsync(() -> {
				IpPlayer pp = IpPlayer.getIpPlayer(cible);
				IP ip = pp.getIP();
				for (String s : Messages.getStringList("messages.getip.online", "%name%", cible.getName(), "%uuid%", cible.getUniqueId().toString(), "%ip%", get(pp.getBasicIP()),
						"%proxy_ip%", get(pp.getBungeeIP()), "%fai%", get(pp.getFaiIP()), "%asn_name%", ip.getASNName(), "%asn%", ip.getASN(), "%vpn%", Messages.getMessage(ip.isVPN()),
						"%proxy%", Messages.getMessage(ip.isProxy()))) {
					if (s == null)
						continue;
					for (IpInfos ii : IpInfos.values())
						s = s.replaceAll("%" + ii.name().toLowerCase() + "%", get(ip.getIpInfos(ii)));
					sender.sendMessage(s);
				}
			});
		} else
			Messages.sendMessage(sender, "messages.not_registered", "%name%", of.getName(), "%uuid%", of.getUniqueId().toString());

		return false;
	}

	private String get(String s) {
		return s == null ? Messages.getMessage("messages.unknow") : s;
	}
}
