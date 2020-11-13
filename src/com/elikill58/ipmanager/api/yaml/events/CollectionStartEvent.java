package com.elikill58.ipmanager.api.yaml.events;

import com.elikill58.ipmanager.api.yaml.error.Mark;

public abstract class CollectionStartEvent extends NodeEvent {
	private final String tag;
	private final boolean implicit;
	private final Boolean flowStyle;

	public CollectionStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark,
			final Mark endMark, final Boolean flowStyle) {
		super(anchor, startMark, endMark);
		this.tag = tag;
		this.implicit = implicit;
		this.flowStyle = flowStyle;
	}

	public String getTag() {
		return this.tag;
	}

	public boolean getImplicit() {
		return this.implicit;
	}

	public Boolean getFlowStyle() {
		return this.flowStyle;
	}

	@Override
	protected String getArguments() {
		return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
	}
}
