package com.elikill58.ipmanager.common.commands;

import static com.elikill58.ipmanager.universal.Messages.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.elikill58.ipmanager.api.commands.CommandListeners;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.commands.TabListeners;
import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.InventoryManager;
import com.elikill58.ipmanager.api.inventory.AbstractInventory.NegativityInventory;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.IP;
import com.elikill58.ipmanager.universal.IP.IpInfos;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.Messages;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;

public class GetIpCommand implements CommandListeners, TabListeners {

	@Override
	public boolean onCommand(CommandSender sender, String[] arg, String prefix) {
		if (arg.length == 0) {
			sender.sendMessage(getMessage("precise"));
			return true;
		}
		Adapter ada = Adapter.getAdapter();
		if (arg[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(getMessage("reloaded"));
			ada.reload();
			IpManager.init();
			return false;
		}
		OfflinePlayer of = ada.getOfflinePlayer(arg[0]);
		if (of == null) {
			try {
				of = ada.getOfflinePlayer(UUID.fromString(arg[0]));
			} catch (Exception e) {
			}
		}
		if (of == null || of.getUniqueId() == null) {
			sender.sendMessage(getMessage("cannot_found"));
			return false;
		}
		if(arg.length >= 2 && arg[1].equalsIgnoreCase("inv") && sender instanceof Player && of instanceof Player && ada.getPlatformID().canUseItem()) {
			InventoryManager.open(NegativityInventory.IP_PLAYER, (Player) sender, (Player) of);
			return false;
		}
		UUID uuid = of.getUniqueId();
		try {
			IpPlayerAccount pp = IpPlayerAccountManager.getManager().getNow(uuid);
			if (of.isOnline()) {
				IP ip = pp.getIP();
				for (String s : Messages.getStringList("getip.online", "%name%", of.getName(), "%uuid%",
						uuid.toString(), "%ip%", get(pp.getBasicIp()), "%proxy_ip%", get(pp.getProxy()), "%fai%",
						get(pp.getFai()), "%vpn%", getMessage(ip.isVPN()), "%proxy%", getMessage(ip.isProxy()),
						"%hosting%", getMessage(ip.isHosting()))) {
					if (s == null)
						continue;
					for (IpInfos ii : IpInfos.values())
						s = s.replaceAll("%" + ii.name().toLowerCase() + "%", get(ip.getIpInfos(ii)));
					sender.sendMessage(s);
				}
			} else {
				Messages.getStringList("getip.offline", "%name%", of.getName(), "%uuid%", uuid.toString(),
						"%ip%", get(pp.getBasicIp()), "%proxy_ip%", get(pp.getProxy()), "%fai%", get(pp.getFai()))
						.forEach(sender::sendMessage);
			}
		} catch (Exception e) {
			sender.sendMessage(
					getMessage("not_registered", "%name%", of.getName(), "%uuid%", uuid.toString()));
			Adapter.getAdapter().getLogger()
					.info("Error while doing getip: " + e.getMessage() + " (" + e.getStackTrace()[0].toString() + ")");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] arg, String prefix) {
		List<String> tab = new ArrayList<>();
		for (Player p : Adapter.getAdapter().getOnlinePlayers())
			if (prefix.isEmpty() || p.getName().toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase()))
				tab.add(p.getName());
		return tab;
	}

	private String get(String s) {
		return s == null ? getMessage("unknow") : s;
	}
}
