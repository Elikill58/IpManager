package com.elikill58.ipmanager.ban;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

public enum BanProcessor {
	
	COMMAND((ban) -> {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), BanManager.getInstance().getCommand().replaceAll("%uuid%", ban.getPlayerId().toString()).replaceAll("%name%", ban.getOfflinePlayer().getName()).replaceAll("%reason%", ban.getReason()));
	});
	
	private final Consumer<Ban> call;
	
	private BanProcessor(Consumer<Ban> call) {
		this.call = call;
	}
	
	public void run(Ban ban) {
		call.accept(ban);
	}
}