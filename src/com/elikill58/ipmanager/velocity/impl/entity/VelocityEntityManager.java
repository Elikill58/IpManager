package com.elikill58.ipmanager.velocity.impl.entity;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.entity.Player;
import com.velocitypowered.api.command.CommandSource;

public class VelocityEntityManager {

	public static Player getPlayer(com.velocitypowered.api.proxy.Player p){
		return Players.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p));
	}

	public static CommandSender getExecutor(CommandSource sender) {
		if(sender == null)
			return null;
		if(sender instanceof com.velocitypowered.api.proxy.Player)
			return new VelocityPlayer((com.velocitypowered.api.proxy.Player) sender);
		return new VelocityCommandSender(sender);
	}
}
