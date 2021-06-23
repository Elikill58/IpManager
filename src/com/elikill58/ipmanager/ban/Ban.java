package com.elikill58.ipmanager.ban;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Ban {

	private final UUID playerId;
	private final String reason;
	private final String bannedBy;

	public Ban(UUID playerId, String reason, String bannedBy) {
		this.playerId = playerId;
		this.reason = reason;
		this.bannedBy = bannedBy;
	}

	public UUID getPlayerId() {
		return playerId;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerId());
	}

	public String getReason() {
		return reason;
	}

	public String getBannedBy() {
		return bannedBy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Ban ban = (Ban) o;
		return playerId.equals(ban.playerId) && reason.equals(ban.reason)
				&& bannedBy.equals(ban.bannedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(playerId, reason, bannedBy);
	}

	public static Ban from(Ban from) {
		return new Ban(from.getPlayerId(), from.getReason(), from.getBannedBy());
	}

	public static Ban create(UUID playerId, String reason, String bannedBy) {
		return new Ban(playerId, reason, bannedBy);
	}
}
