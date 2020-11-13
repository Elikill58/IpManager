package com.elikill58.ipmanager.api.yaml.serializer;

import com.elikill58.ipmanager.api.yaml.error.YAMLException;

public class SerializerException extends YAMLException {
	private static final long serialVersionUID = 2632638197498912433L;

	public SerializerException(final String message) {
		super(message);
	}
}
