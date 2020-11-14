package com.elikill58.ipmanager.spigot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
import com.elikill58.ipmanager.spigot.impl.entity.SpigotOfflinePlayer;
import com.elikill58.ipmanager.spigot.impl.entity.SpigotPlayer;
import com.elikill58.ipmanager.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ipmanager.spigot.impl.item.SpigotItemBuilder;
import com.elikill58.ipmanager.spigot.impl.item.SpigotItemRegistrar;
import com.elikill58.ipmanager.spigot.impl.location.SpigotLocation;
import com.elikill58.ipmanager.spigot.impl.plugin.SpigotExternalPlugin;
import com.elikill58.ipmanager.spigot.utils.PacketUtils;
import com.elikill58.ipmanager.spigot.utils.Utils;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Platform;
import com.elikill58.ipmanager.universal.logger.JavaLoggerAdapter;
import com.elikill58.ipmanager.universal.logger.LoggerAdapter;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class SpigotAdapter extends Adapter {

	private final JavaPlugin pl;
	private final LoggerAdapter logger;
	private final SpigotItemRegistrar itemRegistrar;
	private Configuration config;

	public SpigotAdapter(JavaPlugin pl) {
		this.pl = pl;
		reloadConfig();
		this.logger = new JavaLoggerAdapter(pl.getLogger());
		this.itemRegistrar = new SpigotItemRegistrar();
	}
	
	@Override
	public Platform getPlatformID() {
		return Platform.SPIGOT;
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
		if (UniversalUtils.DEBUG)
			pl.getLogger().info(msg);
	}

	@Nullable
	@Override
	public InputStream openBundledFile(String name) {
		return pl.getResource("assets/ipmanager/" + name);
	}

	@Override
	public void reload() {
		reloadConfig();
	}

	@Override
	public String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	@Override
	public String getPluginVersion() {
		return pl.getDescription().getVersion();
	}

	@Override
	public void reloadConfig() {
		try {
			this.config = YamlConfiguration.load(copyBundledFile("config.yml", new File(pl.getDataFolder(), "config.yml").toPath()).toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runConsoleCommand(String cmd) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	@Override
	public List<UUID> getOnlinePlayersUUID() {
		List<UUID> list = new ArrayList<>();
		for (org.bukkit.entity.Player temp : Utils.getOnlinePlayers())
			list.add(temp.getUniqueId());
		return list;
	}

	@Override
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		for (org.bukkit.entity.Player temp : Utils.getOnlinePlayers())
			list.add(Players.getPlayer(temp.getUniqueId(), () -> new SpigotPlayer(temp)));
		return list;
	}

	@Override
	public LoggerAdapter getLogger() {
		return logger;
	}

	@Override
	public double getLastTPS() {
		double[] tps = getTPS();
		return tps[tps.length - 1];
	}

	@Override
	public double[] getTPS() {
		if(SpigotIpManager.isCraftBukkit) {
			return new double[] {20, 20, 20};
		} else {
			try {
				Class<?> mcServer = PacketUtils.getNmsClass("MinecraftServer");
				Object server = mcServer.getMethod("getServer").invoke(mcServer);
				return (double[]) server.getClass().getField("recentTps").get(server);
			} catch (Exception e) {
				getLogger().warn("Cannot get TPS (Work on Spigot but NOT CraftBukkit).");
				e.printStackTrace();
				return new double[] {20, 20, 20};
			}
		}
	}

	@Override
	public ItemRegistrar getItemRegistrar() {
		return itemRegistrar;
	}

	@Override
	public Location createLocation(World w, double x, double y, double z) {
		return new SpigotLocation(w, x, y, z);
	}

	@Override
	public Inventory createInventory(String inventoryName, int size, NegativityHolder holder) {
		return new SpigotInventory(inventoryName, size, holder);
	}

	@Override
	public void sendMessageRunnableHover(Player p, String message, String hover, String command) {
		new ClickableText().addRunnableHoverEvent(message, hover, command).sendToPlayer((org.bukkit.entity.Player) p.getDefault());
	}

	@Override
	public ItemBuilder createItemBuilder(Material type) {
		return new SpigotItemBuilder(type);
	}
	
	@Override
	public ItemBuilder createItemBuilder(String type) {
		return new SpigotItemBuilder(type);
	}
	
	@Override
	public ItemBuilder createSkullItemBuilder(Player owner) {
		return new SpigotItemBuilder(owner);
	}

	@Override
	public Player getPlayer(String name) {
		org.bukkit.entity.Player p = Bukkit.getPlayer(name);
		if(p == null)
			return null;
		return new SpigotPlayer(p);
	}

	@Override
	public Player getPlayer(UUID uuid) {
		org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
		if(p == null)
			return null;
		return new SpigotPlayer(p);
	}

	@SuppressWarnings("deprecation")
	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		Player tempP = getPlayer(name);
		if(tempP != null)
			return tempP;
		org.bukkit.OfflinePlayer p = Bukkit.getOfflinePlayer(name);
		if(p == null)
			return null;
		return new SpigotOfflinePlayer(p);
	}
	
	@Override
	public OfflinePlayer getOfflinePlayer(UUID uuid) {
		Player tempP = getPlayer(uuid);
		if(tempP != null)
			return tempP;
		org.bukkit.OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
		if(p == null)
			return null;
		return new SpigotOfflinePlayer(p);
	}
	
	@Override
	public boolean hasPlugin(String name) {
		return Bukkit.getPluginManager().getPlugin(name) != null;
	}
	
	@Override
	public ExternalPlugin getPlugin(String name) {
		return new SpigotExternalPlugin(Bukkit.getPluginManager().getPlugin(name));
	}
	
	@Override
	public void runSync(Runnable call) {
		Bukkit.getScheduler().runTask(pl, call);
	}
}
