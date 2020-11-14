package com.elikill58.ipmanager.sponge.impl.entity;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.entity.Entity;

public class SpongeEntityManager {

	public static com.elikill58.ipmanager.api.entity.Player getPlayer(Player p){
		return Players.getPlayer(p.getUniqueId(), () -> new SpongePlayer(p));
	}
	
	public static Entity getEntity(org.spongepowered.api.entity.Entity e) {
		if(e == null)
			return null;
		if(e.getType().equals(EntityTypes.PLAYER))
			return getPlayer((Player) e);
		else
			return new SpongeEntity(e);
	}

	public static CommandSender getExecutor(CommandSource src) {
		if(src == null)
			return null;
		if(src instanceof Player)
			return new SpongePlayer((Player) src);
		return new SpongeCommandSender(src);
	}
}
