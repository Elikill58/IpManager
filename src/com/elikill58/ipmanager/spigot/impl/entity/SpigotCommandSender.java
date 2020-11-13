package com.elikill58.ipmanager.spigot.impl.entity;

import com.elikill58.ipmanager.api.commands.CommandSender;

public class SpigotCommandSender extends CommandSender {

	private final org.bukkit.command.CommandSender sender;
	
	public SpigotCommandSender(org.bukkit.command.CommandSender sender) {
		this.sender = sender;
	}
	
	@Override
	public void sendMessage(String msg) {
		sender.sendMessage(msg);
	}

	@Override
	public String getName() {
		return sender.getName();
	}
	
	@Override
	public Object getDefault() {
		return sender;
	}
}
