package com.elikill58.ipmanager.api.commands;

import java.util.HashMap;

import com.elikill58.ipmanager.api.events.EventListener;
import com.elikill58.ipmanager.api.events.Listeners;
import com.elikill58.ipmanager.api.events.others.CommandExecutionEvent;
import com.elikill58.ipmanager.api.events.others.TabExecutionEvent;
import com.elikill58.ipmanager.common.commands.GetIpCommand;

public class CommandManager implements Listeners {

	private final HashMap<String, CommandListeners> commands = new HashMap<>();
	private final HashMap<String, TabListeners> tabs = new HashMap<>();
	
	public CommandManager() {
		GetIpCommand negativity = new GetIpCommand();
		commands.put("getip", negativity);
		tabs.put("getip", negativity);
	}
	
	@EventListener
	public void onCommand(CommandExecutionEvent e) {
		CommandListeners cmd = commands.get(e.getCommand().toLowerCase());
		if(cmd != null)
			e.setGoodResult(cmd.onCommand(e.getSender(), e.getArgument(), e.getPrefix()));
	}
	
	@EventListener
	public void onTab(TabExecutionEvent e) {
		TabListeners cmd = tabs.get(e.getCommand().toLowerCase());
		if(cmd != null)
			e.setTabContent(cmd.onTabComplete(e.getSender(), e.getArgument(), e.getPrefix()));
	}
	
}
