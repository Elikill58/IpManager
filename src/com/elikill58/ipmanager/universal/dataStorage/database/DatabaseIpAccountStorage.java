package com.elikill58.ipmanager.universal.dataStorage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.account.IpManagerAccount;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;

public class DatabaseIpAccountStorage extends IpManagerAccountStorage {

	public DatabaseIpAccountStorage() {
		try {
			Connection connection = Database.getConnection();
			if (connection != null) {
				DatabaseMigrator.executeRemainingMigrations(connection, "accounts");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompletableFuture<IpManagerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.supplyAsync(() -> {
			try (PreparedStatement stm = Database.getConnection().prepareStatement("SELECT * FROM ipmanager_accounts WHERE id = ?")) {
				stm.setString(1, playerId.toString());
				ResultSet result = stm.executeQuery();
				if (result.next()) {
					String playerName = result.getString("playername");
					String IP = result.getString("ip");
					String proxy = result.getString("proxy");
					String fai = result.getString("fai");
					String connection = result.getString("connection");
					long creationTime = result.getTimestamp("creation_time").getTime();
					return new IpManagerAccount(playerId, playerName, IP, proxy, fai, deserializeConnections(connection), creationTime);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new IpManagerAccount(playerId);
		});
	}

	@Override
	public CompletableFuture<Void> saveAccount(IpManagerAccount account) {
		return CompletableFuture.runAsync(() -> {
			try (PreparedStatement stm = Database.getConnection().prepareStatement(
					"REPLACE INTO ipmanager_accounts (id, playername, ip, proxy, fai, connection, creation_time) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
				stm.setString(1, account.getPlayerId().toString());
				stm.setString(2, account.getPlayerName());
				stm.setString(3, account.getIp());
				stm.setString(4, account.getProxy());
				stm.setString(5, account.getFai());
				stm.setString(6, serializeConnections(account));
				stm.setTimestamp(7, new Timestamp(account.getCreationTime()));
				stm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	private static String serializeConnections(IpManagerAccount account) {
		StringJoiner joiner = new StringJoiner(";");
		account.getAllConnections().forEach((l) -> joiner.add(String.valueOf(l)));
		return joiner.toString();
	}

	private static List<Long> deserializeConnections(String serialized) {
		List<Long>  connections = new ArrayList<>();
		String[] fullEntries = serialized.split(";");
		for (String fullEntry : fullEntries) {
			connections.add(Long.parseLong(fullEntry));
		}
		return connections;
	}
	
	@Override
	public List<UUID> getPlayersOnIP(String ip) {
		return CompletableFuture.supplyAsync(() -> {
			List<UUID> list = new ArrayList<>();
			try (PreparedStatement stm = Database.getConnection().prepareStatement("SELECT * FROM ipmanager_accounts WHERE ip = ?")) {
				stm.setString(1, ip);
				ResultSet result = stm.executeQuery();
				while (result.next()) {
					list.add(UUID.fromString(result.getString("id")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}).join();
	}
}
