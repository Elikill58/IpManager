package com.elikill58.ipmanager.sponge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

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
import com.elikill58.ipmanager.sponge.impl.entity.SpongeEntityManager;
import com.elikill58.ipmanager.sponge.impl.entity.SpongeOfflinePlayer;
import com.elikill58.ipmanager.sponge.impl.inventory.SpongeInventory;
import com.elikill58.ipmanager.sponge.impl.item.SpongeItemBuilder;
import com.elikill58.ipmanager.sponge.impl.item.SpongeItemRegistrar;
import com.elikill58.ipmanager.sponge.impl.location.SpongeLocation;
import com.elikill58.ipmanager.sponge.impl.plugin.SpongeExternalPlugin;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Platform;
import com.elikill58.ipmanager.universal.logger.LoggerAdapter;
import com.elikill58.ipmanager.universal.logger.Slf4jLoggerAdapter;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class SpongeAdapter extends Adapter {

	private final LoggerAdapter logger;
	private final SpongeIpManager plugin;
	private Configuration config;
	private final SpongeItemRegistrar itemRegistrar;

	public SpongeAdapter(SpongeIpManager sn) {
		this.plugin = sn;
		this.logger = new Slf4jLoggerAdapter(sn.getLogger());
		this.config = UniversalUtils.loadConfig(new File(getDataFolder(), "config.yml"), "config.yml");
		this.itemRegistrar = new SpongeItemRegistrar();
	}
	
	@Override
	public Platform getPlatformID() {
		return Platform.SPONGE;
	}

	@Override
	public Configuration getConfig() {
		return config;
	}

	@Override
	public File getDataFolder() {
		return plugin.getDataFolder().toFile();
	}

	@Override
	public void debug(String msg) {
		if(UniversalUtils.DEBUG)
			logger.info(msg);
	}

	@Nullable
	@Override
	public InputStream openBundledFile(String name) throws IOException {
		Asset asset = plugin.getContainer().getAsset(name).orElse(null);
		return asset == null ? null : asset.getUrl().openStream();
	}

	@Override
	public void reload() {
		reloadConfig();
	}

	@Override
	public String getVersion() {
		return Sponge.getPlatform().getMinecraftVersion().getName();
	}
	
	@Override
	public String getPluginVersion() {
		return plugin.getContainer().getVersion().orElse(UniversalUtils.NEGATIVITY_VERSION);
	}

	@Override
	public void reloadConfig() {
		this.config = UniversalUtils.loadConfig(new File(getDataFolder(), "config.yml"), "config.yml");
	}

	@Override
	public void runConsoleCommand(String cmd) {
		Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd);
	}

	@Override
	public LoggerAdapter getLogger() {
		return logger;
	}

	@Override
	public List<UUID> getOnlinePlayersUUID() {
		List<UUID> list = new ArrayList<>();
		for (org.spongepowered.api.entity.living.player.Player temp : Sponge.getServer().getOnlinePlayers())
			list.add(temp.getUniqueId());
		return list;
	}

	@Override
	public double[] getTPS() {
		return new double[] {getLastTPS()};
	}

	@Override
	public double getLastTPS() {
		return Sponge.getServer().getTicksPerSecond();
	}

	@Override
	public ItemRegistrar getItemRegistrar() {
		return itemRegistrar;
	}

	@Override
	public Location createLocation(World w, double x, double y, double z) {
		return new SpongeLocation(w, x, y, z);
	}

	@Override
	public void sendMessageRunnableHover(Player p, String message, String hover,
			String command) {
		((org.spongepowered.api.entity.living.player.Player) p.getDefault()).sendMessage(Text.builder(message).onHover(TextActions.showText(Text.of(hover))).build());
	}

	@Override
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		for (org.spongepowered.api.entity.living.player.Player temp : Sponge.getServer().getOnlinePlayers())
			list.add(SpongeEntityManager.getPlayer(temp));
		return list;
	}

	@Override
	public ItemBuilder createItemBuilder(Material type) {
		return new SpongeItemBuilder(type);
	}
	
	@Override
	public ItemBuilder createItemBuilder(String type) {
		return new SpongeItemBuilder(itemRegistrar.get(type.split(":")[0]));
	}
	
	@Override
	public ItemBuilder createSkullItemBuilder(Player owner) {
		return new SpongeItemBuilder(owner);
	}

	@Override
	public Inventory createInventory(String inventoryName, int size, NegativityHolder holder) {
		return new SpongeInventory(inventoryName, size, holder);
	}

	@Override
	public Player getPlayer(String name) {
		return SpongeEntityManager.getPlayer(Sponge.getServer().getPlayer(name).orElse(null));
	}

	@Override
	public Player getPlayer(UUID uuid) {
		return SpongeEntityManager.getPlayer(Sponge.getServer().getPlayer(uuid).orElse(null));
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		Player tempP = getPlayer(name);
		if(tempP != null)
			return tempP;
		Optional<User> optUser = Sponge.getServiceManager().provide(UserStorageService.class).get().get(name);
	    return optUser.isPresent() ? new SpongeOfflinePlayer(optUser.get()) : null;
	}
	
	@Override
	public OfflinePlayer getOfflinePlayer(UUID uuid) {
		Player tempP = getPlayer(uuid);
		if(tempP != null)
			return tempP;
		Optional<User> optUser = Sponge.getServiceManager().provide(UserStorageService.class).get().get(uuid);
	    return optUser.isPresent() ? new SpongeOfflinePlayer(optUser.get()) : null;
	}

	@Override
	public boolean hasPlugin(String name) {
		return Sponge.getPluginManager().isLoaded(name);
	}

	@Override
	public ExternalPlugin getPlugin(String name) {
		return new SpongeExternalPlugin(Sponge.getPluginManager().getPlugin(name).orElse(null));
	}
	
	@Override
	public void runSync(Runnable call) {
		Task.builder().execute(call).submit(plugin);
	}
}
