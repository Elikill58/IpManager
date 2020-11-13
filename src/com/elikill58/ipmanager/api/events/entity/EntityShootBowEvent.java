package com.elikill58.ipmanager.api.events.entity;

import com.elikill58.ipmanager.api.entity.Entity;
import com.elikill58.ipmanager.api.events.Event;

public class EntityShootBowEvent implements Event {
	
	private final Entity et;
	
	public EntityShootBowEvent(Entity shoot) {
		this.et = shoot;
	}
	
	public Entity getEntity() {
		return et;
	}
}
