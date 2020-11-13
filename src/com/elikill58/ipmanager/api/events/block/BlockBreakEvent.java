package com.elikill58.ipmanager.api.events.block;

import com.elikill58.ipmanager.api.block.Block;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;

public class BlockBreakEvent implements Event {

	private final Player p;
	private final Block b;
	private boolean cancel;
	
	public BlockBreakEvent(Player p, Block b) {
		this.p = p;
		this.b = b;
	}
	
	public Block getBlock() {
		return b;
	}
	
	public Player getPlayer() {
		return p;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
