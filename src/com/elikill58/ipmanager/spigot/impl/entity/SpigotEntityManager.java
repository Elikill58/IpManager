package com.elikill58.ipmanager.spigot.impl.entity;

import org.bukkit.entity.Player;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.entity.Entity;

public class SpigotEntityManager {

	public static com.elikill58.ipmanager.api.entity.Player getPlayer(Player p){
		return IpPlayer.getPlayer(p.getUniqueId(), () -> new SpigotPlayer(p)).getPlayer();
	}
	
	public static Entity getEntity(org.bukkit.entity.Entity bukkitEntity) {
		if(bukkitEntity == null)
			return null;
		switch (bukkitEntity.getType()) {
		case PLAYER:
			return getPlayer((Player) bukkitEntity);
		default:
			return new SpigotEntity(bukkitEntity);
		}
	}

	public static CommandSender getExecutor(org.bukkit.command.CommandSender sender) {
		if(sender == null)
			return null;
		if(sender instanceof Player)
			return new SpigotPlayer((Player) sender);
		return new SpigotCommandSender(sender);
	}
}
