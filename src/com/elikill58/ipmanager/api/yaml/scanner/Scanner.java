package com.elikill58.ipmanager.api.yaml.scanner;

import com.elikill58.ipmanager.api.yaml.tokens.Token;

public interface Scanner {
	boolean checkToken(final Token.ID... p0);

	Token peekToken();

	Token getToken();
}
