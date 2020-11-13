package com.elikill58.ipmanager.velocity.impl.entity;

import com.elikill58.ipmanager.api.commands.CommandSender;
import com.velocitypowered.api.command.CommandSource;

import net.kyori.text.TextComponent;

public class VelocityCommandSender extends CommandSender {

	private final CommandSource sender;
	
	public VelocityCommandSender(CommandSource sender) {
		this.sender = sender;
	}
	
	@Override
	public void sendMessage(String msg) {
		sender.sendMessage(TextComponent.of(msg));
	}

	@Override
	public String getName() {
		return "No Name";
	}
	
	@Override
	public Object getDefault() {
		return sender;
	}
}
