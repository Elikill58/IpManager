package com.elikill58.ipmanager.exception;

@SuppressWarnings("serial")
public class NotLoadedException extends Exception {
	public NotLoadedException(String errorMessage) {
        super(errorMessage);
    }
}
