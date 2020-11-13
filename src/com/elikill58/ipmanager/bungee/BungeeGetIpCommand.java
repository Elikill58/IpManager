package com.elikill58.ipmanager.bungee;

import java.util.Locale;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.others.CommandExecutionEvent;
import com.elikill58.ipmanager.api.events.others.TabExecutionEvent;
import com.elikill58.ipmanager.bungee.impl.entity.BungeeEntityManager;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeGetIpCommand extends Command implements Listener {

	public BungeeGetIpCommand() {
		super("getip");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		EventManager.callEvent(new CommandExecutionEvent("getip", BungeeEntityManager.getExecutor(sender), args, ""));
	}

	@EventHandler
	public void complete(TabCompleteEvent event) {
		String[] arg = event.getCursor().split(" ");
		String prefix = arg.length == 0 ? "" : arg[arg.length - 1].toLowerCase(Locale.ROOT);
		TabExecutionEvent ev = new TabExecutionEvent("getip", BungeeEntityManager.getExecutor((CommandSender) event.getSender()), arg, prefix);
		EventManager.callEvent(ev);
		
		event.getSuggestions().clear();
		event.getSuggestions().addAll(ev.getTabContent());
	}
}
