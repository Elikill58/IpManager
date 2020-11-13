package com.elikill58.ipmanager.sponge.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.block.BlockBreakEvent;
import com.elikill58.ipmanager.api.events.block.BlockPlaceEvent;
import com.elikill58.ipmanager.sponge.impl.block.SpongeBlock;
import com.elikill58.ipmanager.sponge.impl.entity.SpongeEntityManager;

public class BlockListeners {

	@Listener
	public void onBlockBreak(ChangeBlockEvent.Break e, @First Player p) {
		BlockBreakEvent event = new BlockBreakEvent(SpongeEntityManager.getPlayer(p), new SpongeBlock(e.getTransactions().get(0).getOriginal()));
		EventManager.callEvent(event);
		e.setCancelled(event.isCancelled());
	}

	@Listener
	public void onBlockPlace(ChangeBlockEvent.Place e, @First Player p) {
		BlockPlaceEvent event = new BlockPlaceEvent(SpongeEntityManager.getPlayer(p), new SpongeBlock(e.getTransactions().get(0).getOriginal()));
		EventManager.callEvent(event);
		e.setCancelled(event.isCancelled());
	}
}
