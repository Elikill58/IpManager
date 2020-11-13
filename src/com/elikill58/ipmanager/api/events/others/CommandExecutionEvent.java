package com.elikill58.ipmanager.api.events.others;

import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.events.Event;

public class CommandExecutionEvent implements Event {

	private final CommandSender sender;
	private final String[] arg;
	private final String command, prefix;
	private boolean hasGoodResult = true;

	public CommandExecutionEvent(String command, CommandSender sender, String[] arg, String prefix) {
		this.command = command;
		this.sender = sender;
		this.arg = arg;
		this.prefix = prefix;
	}

	public String getCommand() {
		return command;
	}

	public CommandSender getSender() {
		return sender;
	}

	public String[] getArgument() {
		return arg;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean hasGoodResult() {
		return hasGoodResult;
	}
	
	public void setGoodResult(boolean hasGoodResult) {
		this.hasGoodResult = hasGoodResult;
	}
}
