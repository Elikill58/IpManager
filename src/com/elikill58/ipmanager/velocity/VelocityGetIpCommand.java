package com.elikill58.ipmanager.velocity;

import java.util.List;
import java.util.Locale;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.others.CommandExecutionEvent;
import com.elikill58.ipmanager.api.events.others.TabExecutionEvent;
import com.elikill58.ipmanager.velocity.impl.entity.VelocityEntityManager;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;

public class VelocityGetIpCommand implements Command {

	@Override
	public void execute(CommandSource source, @NonNull String[] args) {
		EventManager.callEvent(new CommandExecutionEvent("getip", VelocityEntityManager.getExecutor(source), args, ""));
	}

	@Override
	public List<String> suggest(CommandSource source, @NonNull String[] arg) {
		String prefix = arg.length == 0 ? "" : arg[arg.length - 1].toLowerCase(Locale.ROOT);
		TabExecutionEvent ev = new TabExecutionEvent("getip", VelocityEntityManager.getExecutor(source), arg, prefix);
		EventManager.callEvent(ev);
		return ev.getTabContent();
	}
}
