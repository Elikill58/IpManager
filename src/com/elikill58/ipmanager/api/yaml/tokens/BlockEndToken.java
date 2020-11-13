package com.elikill58.ipmanager.api.yaml.tokens;

import com.elikill58.ipmanager.api.yaml.error.Mark;

public final class BlockEndToken extends Token {
	public BlockEndToken(final Mark startMark, final Mark endMark) {
		super(startMark, endMark);
	}

	@Override
	public ID getTokenId() {
		return ID.BlockEnd;
	}
}
