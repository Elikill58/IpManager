package com.elikill58.ipmanager.api.yaml.tokens;

public final class TagTuple {
	private final String handle;
	private final String suffix;

	public TagTuple(final String handle, final String suffix) {
		if (suffix == null) {
			throw new NullPointerException("Suffix must be provided.");
		}
		this.handle = handle;
		this.suffix = suffix;
	}

	public String getHandle() {
		return this.handle;
	}

	public String getSuffix() {
		return this.suffix;
	}
}
