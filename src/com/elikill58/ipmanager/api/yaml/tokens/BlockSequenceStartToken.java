package com.elikill58.ipmanager.api.yaml.tokens;

import com.elikill58.ipmanager.api.yaml.error.Mark;

public final class BlockSequenceStartToken extends Token {
	public BlockSequenceStartToken(final Mark startMark, final Mark endMark) {
		super(startMark, endMark);
	}

	@Override
	public ID getTokenId() {
		return ID.BlockSequenceStart;
	}
}