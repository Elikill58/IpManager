package com.elikill58.ipmanager.api.yaml.serializer;

import com.elikill58.ipmanager.api.yaml.nodes.Node;

public interface AnchorGenerator {
	String nextAnchor(final Node p0);
}
