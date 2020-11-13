package com.elikill58.ipmanager.bungee.impl.entity;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.entity.Player;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeEntityManager {

	public static Player getPlayer(ProxiedPlayer p){
		return IpPlayer.getPlayer(p.getUniqueId(), () -> new BungeePlayer(p)).getPlayer();
	}

	public static CommandSender getExecutor(net.md_5.bungee.api.CommandSender sender) {
		if(sender == null)
			return null;
		if(sender instanceof ProxiedPlayer)
			return new BungeePlayer((ProxiedPlayer) sender);
		return new BungeeCommandSender(sender);
	}
}
