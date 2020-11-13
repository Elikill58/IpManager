package com.elikill58.ipmanager.api.yaml.emitter;

import java.io.IOException;

import com.elikill58.ipmanager.api.yaml.events.Event;

public interface Emitable {
	void emit(final Event p0) throws IOException;
}
