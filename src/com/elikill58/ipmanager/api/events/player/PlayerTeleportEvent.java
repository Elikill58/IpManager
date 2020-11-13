package com.elikill58.ipmanager.api.events.player;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.Event;
import com.elikill58.ipmanager.api.location.Location;

public class PlayerTeleportEvent implements Event {

	private final Player p;
	private final Location from, to;
	
	public PlayerTeleportEvent(Player p, Location from, Location to) {
		this.p = p;
		this.from = from;
		this.to = to;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Location getFrom() {
		return from;
	}
	
	public Location getTo() {
		return to;
	}
}
