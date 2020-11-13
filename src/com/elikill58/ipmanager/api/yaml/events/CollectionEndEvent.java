package com.elikill58.ipmanager.api.yaml.events;

import com.elikill58.ipmanager.api.yaml.error.Mark;

public abstract class CollectionEndEvent extends Event {
	public CollectionEndEvent(final Mark startMark, final Mark endMark) {
		super(startMark, endMark);
	}
}
