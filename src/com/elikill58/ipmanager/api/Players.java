package com.elikill58.ipmanager.api;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.universal.Adapter;

public class Players {
	public static final HashMap<UUID, Player> PLAYERS = new HashMap<>();

	public static Player getPlayer(UUID uuid) {
		return getPlayer(uuid, () -> null);
	}

	public static Player getPlayer(UUID uuid, Callable<Player> call) {
		return PLAYERS.computeIfAbsent(uuid, (id) -> {
			try {
				return call.call();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public static List<Player> getPlayersOnIP(String IP){
		return Adapter.getAdapter().getOnlinePlayers().stream().filter((p) -> p.getIP().equalsIgnoreCase(IP)).collect(Collectors.toList());
	}
}
