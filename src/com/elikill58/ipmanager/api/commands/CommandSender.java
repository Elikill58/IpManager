package com.elikill58.ipmanager.api.commands;

import com.elikill58.ipmanager.api.IpManagerObject;

public abstract class CommandSender extends IpManagerObject {
	
	public abstract void sendMessage(String msg);

	public abstract String getName();
}
