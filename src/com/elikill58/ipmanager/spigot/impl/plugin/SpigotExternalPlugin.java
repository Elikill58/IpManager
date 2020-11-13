package com.elikill58.ipmanager.spigot.impl.plugin;

import org.bukkit.plugin.Plugin;

import com.elikill58.ipmanager.api.plugin.ExternalPlugin;

public class SpigotExternalPlugin extends ExternalPlugin {

	private final Plugin pl;
	
	public SpigotExternalPlugin(Plugin plugin) {
		this.pl = plugin;
	}
	
	@Override
	public boolean isEnabled() {
		return pl.isEnabled();
	}

	@Override
	public Object getDefault() {
		return pl;
	}

}
