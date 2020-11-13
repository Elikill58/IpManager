package com.elikill58.ipmanager.bungee.impl.entity;

import com.elikill58.ipmanager.api.commands.CommandSender;

import net.md_5.bungee.api.chat.ComponentBuilder;

public class BungeeCommandSender extends CommandSender {

	private final net.md_5.bungee.api.CommandSender sender;
	
	public BungeeCommandSender(net.md_5.bungee.api.CommandSender sender) {
		this.sender = sender;
	}
	
	@Override
	public void sendMessage(String msg) {
		sender.sendMessage(new ComponentBuilder(msg).create());
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
