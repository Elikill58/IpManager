package com.elikill58.ipmanager.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.NegativityHolder;
import com.elikill58.ipmanager.api.item.ItemBuilder;
import com.elikill58.ipmanager.api.item.ItemRegistrar;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.World;
import com.elikill58.ipmanager.api.plugin.ExternalPlugin;
import com.elikill58.ipmanager.api.yaml.config.Configuration;
import com.elikill58.ipmanager.api.yaml.config.YamlConfiguration;
import com.elikill58.ipmanager.bungee.impl.entity.BungeePlayer;
import com.elikill58.ipmanager.bungee.impl.plugin.BungeeExternalPlugin;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Platform;
import com.elikill58.ipmanager.universal.logger.JavaLoggerAdapter;
import com.elikill58.ipmanager.universal.logger.LoggerAdapter;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeAdapter extends Adapter {

	private Configuration config;
	private final Plugin pl;
	private final LoggerAdapter logger;

	public BungeeAdapter(Plugin pl) {
		this.pl = pl;
		try {
			this.config = YamlConfiguration.load(copyBundledFile("config.yml", new File(pl.getDataFolder(), "config.yml").toPath()).toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.logger = new JavaLoggerAdapter(pl.getLogger());
	}
	
	@Override
	public Platform getPlatformID() {
		return Platform.BUNGEE;
	}

	@Override
	public Configuration getConfig() {
		return config;
	}

	@Override
	public File getDataFolder() {
		return pl.getDataFolder();
	}

	@Override
	public void debug(String msg) {
		if(UniversalUtils.DEBUG)
			getLogger().info(msg);
	}

	@Nullable
	@Override
	public InputStream openBundledFile(String name) {
		return pl.getResourceAsStream("assets/ipmanager/" + name);
	}

	@Override
	public void reload() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public String getVersion() {
		return ProxyServer.getInstance().getGameVersion();
	}
	
	@Override
	public String getPluginVersion() {
		return pl.getDescription().getVersion();
	}

	@Override
	public void reloadConfig() {

	}

	@Override
	public void runConsoleCommand(String cmd) {
		pl.getProxy().getPluginManager().dispatchCommand(pl.getProxy().getConsole(), cmd);
	}

	@Override
	public LoggerAdapter getLogger() {
		return logger;
	}

	@Override
	public List<UUID> getOnlinePlayersUUID() {
		List<UUID> list = new ArrayList<>();
		pl.getProxy().getPlayers().forEach((p) -> list.add(p.getUniqueId()));
		return list;
	}

	@Override
	public double[] getTPS() {
		return null;
	}

	@Override
	public double getLastTPS() {
		return 0;
	}

	@Override
	public void sendMessageRunnableHover(Player p, String message, String hover, String command) {
		
	}

	@Override
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		pl.getProxy().getPlayers().forEach((p) -> list.add(Players.getPlayer(p.getUniqueId(), () -> new BungeePlayer(p))));
		return list;
	}

	@Override
	public Player getPlayer(String name) {
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(name);
		if(pp == null)
			return null;
		return Players.getPlayer(pp.getUniqueId(), () -> new BungeePlayer(pp));
	}

	@Override
	public Player getPlayer(UUID uuid) {
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(uuid);
		if(pp == null)
			return null;
		return Players.getPlayer(uuid, () -> new BungeePlayer(pp));
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		return null;
	}
	
	@Override
	public OfflinePlayer getOfflinePlayer(UUID uuid) {
		// TODO add support for offline bungee players
		return null;
	}
	
	@Override
	public boolean hasPlugin(String name) {
		return pl.getProxy().getPluginManager().getPlugin(name) != null;
	}

	@Override
	public ExternalPlugin getPlugin(String name) {
		return new BungeeExternalPlugin(pl.getProxy().getPluginManager().getPlugin(name));
	}
	
	@Override
	public void runSync(Runnable call) {
		BungeeCord.getInstance().getScheduler().schedule(pl, call, 0, TimeUnit.MILLISECONDS);
	}

	@Override
	public ItemRegistrar getItemRegistrar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemBuilder createItemBuilder(Material type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemBuilder createItemBuilder(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemBuilder createSkullItemBuilder(Player owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location createLocation(World w, double x, double y, double z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventory(String inventoryName, int size, NegativityHolder holder) {
		// TODO Auto-generated method stub
		return null;
	}
}
