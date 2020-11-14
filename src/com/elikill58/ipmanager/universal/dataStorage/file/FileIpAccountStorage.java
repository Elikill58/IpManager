package com.elikill58.ipmanager.universal.dataStorage.file;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.elikill58.ipmanager.api.yaml.config.Configuration;
import com.elikill58.ipmanager.api.yaml.config.YamlConfiguration;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;

public class FileIpAccountStorage extends IpManagerAccountStorage {

	private final File userDir;

	public FileIpAccountStorage(File userDir) {
		this.userDir = userDir;
	}

	@Override
	public CompletableFuture<IpPlayerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.supplyAsync(() -> {
			File file = new File(userDir, playerId + ".yml");
			if (!file.exists()) {
				return new IpPlayerAccount(playerId);
			}
			Configuration config = YamlConfiguration.load(file);
			String playerName = config.getString("playername");
			String IP = config.getString("ip", "0.0.0.0");
			String proxy = config.getString("proxy");
			String fai = config.getString("fai");
			List<Long> connection = config.getLongList("connection");
			long creationTime = config.getLong("creation-time", System.currentTimeMillis());
			return new IpPlayerAccount(playerId, playerName, IP, proxy, fai, connection, creationTime);
		});
	}

	@Override
	public CompletableFuture<Void> saveAccount(IpPlayerAccount account) {
		return CompletableFuture.runAsync(() -> {
			File file = new File(userDir, account.getPlayerId() + ".yml");
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Configuration accountConfig = YamlConfiguration.load(file);
			accountConfig.set("playername", account.getPlayerName());
			accountConfig.set("ip", account.getBasicIp());
			accountConfig.set("proxy", account.getProxy());
			accountConfig.set("fai", account.getFai());
			accountConfig.set("connection", account.getAllConnections());
			accountConfig.set("creation-time", account.getCreationTime());
			accountConfig.save();
		});
	}
	
	@Override
	public List<UUID> getPlayersOnIP(String ip) {
		// TODO Implement getting players on IP when using file system
		return Collections.emptyList();
	}
}
