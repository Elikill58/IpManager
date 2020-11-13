package com.elikill58.ipmanager.api.yaml.emitter;

import com.elikill58.ipmanager.api.yaml.error.YAMLException;

public class EmitterException extends YAMLException {
	private static final long serialVersionUID = -8280070025452995908L;

	public EmitterException(final String msg) {
		super(msg);
	}
}
