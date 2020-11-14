package com.elikill58.ipmanager.velocity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Platform;
import com.elikill58.ipmanager.universal.logger.LoggerAdapter;
import com.elikill58.ipmanager.universal.logger.Slf4jLoggerAdapter;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;
import com.elikill58.ipmanager.velocity.impl.entity.VelocityPlayer;
import com.elikill58.ipmanager.velocity.impl.plugin.VelocityExternalPlugin;

public class VelocityAdapter extends Adapter {

	private Configuration config;
	private VelocityIpManager pl;
	private final LoggerAdapter logger;

	public VelocityAdapter(VelocityIpManager pl) {
		this.pl = pl;
		try {
			this.config = YamlConfiguration.load(copyBundledFile("config.yml", new File(pl.getDataFolder(), "config.yml").toPath()).toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.logger = new Slf4jLoggerAdapter(pl.getLogger());
	}
	
	@Override
	public Platform getPlatformID() {
		return Platform.VELOCITY;
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

	@Override
	public String getVersion() {
		return pl.getServer().getVersion().getVersion();
	}
	
	@Override
	public String getPluginVersion() {
		return UniversalUtils.IP_MANAGER_VERSION;
	}

	@Override
	public void reloadConfig() {

	}

	@Override
	public void runConsoleCommand(String cmd) {
		pl.getServer().getCommandManager().execute(pl.getServer().getConsoleCommandSource(), cmd);
	}

	@Override
	public LoggerAdapter getLogger() {
		return logger;
	}

	@Override
	public List<UUID> getOnlinePlayersUUID() {
		List<UUID> list = new ArrayList<>();
		pl.getServer().getAllPlayers().forEach((p) -> list.add(p.getUniqueId()));
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
	public void sendMessageRunnableHover(com.elikill58.ipmanager.api.entity.Player p, String message, String hover,
			String command) {
		
	}

	@Override
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		pl.getServer().getAllPlayers().forEach((p) -> list.add(Players.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p))));
		return list;
	}

	@Override
	public Player getPlayer(String name) {
		Optional<com.velocitypowered.api.proxy.Player> opt = pl.getServer().getPlayer(name);
		if(opt.isPresent()) {
			com.velocitypowered.api.proxy.Player p = opt.get();
			return Players.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p));
		} else
			return null;
	}

	@Override
	public Player getPlayer(UUID uuid) {
		Optional<com.velocitypowered.api.proxy.Player> opt = pl.getServer().getPlayer(uuid);
		if(opt.isPresent()) {
			return Players.getPlayer(uuid, () -> new VelocityPlayer(opt.get()));
		} else
			return null;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		return null;
	}
	
	@Override
	public OfflinePlayer getOfflinePlayer(UUID uuid) {
		return null;
	}

	@Override
	public boolean hasPlugin(String name) {
		return pl.getServer().getPluginManager().isLoaded(name);
	}

	@Override
	public ExternalPlugin getPlugin(String name) {
		return new VelocityExternalPlugin(pl.getServer().getPluginManager().getPlugin(name).orElse(null));
	}
	
	@Override
	public void runSync(Runnable call) {
		pl.getServer().getScheduler().buildTask(pl, call);
	}
	
	@Override
	public Inventory createInventory(String inventoryName, int size, NegativityHolder holder) {
		// TODO Auto-generated method stub
		return null;
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
}
