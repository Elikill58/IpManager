package com.elikill58.ipmanager.sponge;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.elikill58.ipmanager.sponge.listeners.BlockListeners;
import com.elikill58.ipmanager.sponge.listeners.CommandsListeners;
import com.elikill58.ipmanager.sponge.listeners.EntityListeners;
import com.elikill58.ipmanager.sponge.listeners.InventoryListeners;
import com.elikill58.ipmanager.sponge.listeners.PlayersListeners;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;
import com.google.inject.Inject;

@Plugin(id = "ipmanager", name = "IpManager", version = UniversalUtils.IP_MANAGER_VERSION, description = "Manage player IP - Remove security problem with proxy", authors = {
		"Elikill58" })
public class SpongeIpManager {

	public static SpongeIpManager INSTANCE;

	@Inject
	private PluginContainer plugin;
	@Inject
	public Logger logger;
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	public PluginContainer getContainer() {
		return plugin;
	}

	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		INSTANCE = this;

		Adapter.setAdapter(new SpongeAdapter(this));

		IpManager.init();

		EventManager eventManager = Sponge.getEventManager();
		eventManager.registerListeners(this, new BlockListeners());
		eventManager.registerListeners(this, new EntityListeners());
		eventManager.registerListeners(this, new InventoryListeners());
		eventManager.registerListeners(this, new PlayersListeners());

		IpManagerAccountStorage.setDefaultStorage("file");

		plugin.getLogger().info("IpManager v" + plugin.getVersion().get() + " loaded.");
	}

	@Listener
	public void onGameStop(GameStoppingServerEvent e) {
		Database.close();
	}

	@Listener
	public void onGameStart(GameStartingServerEvent e) {
		CommandManager cmd = Sponge.getCommandManager();
		cmd.register(this, new CommandsListeners("getip"), "getip");
	}

	@Listener
	public void onGameReload(GameReloadEvent event) {
		Adapter.getAdapter().reload();
	}

	public static SpongeIpManager getInstance() {
		return INSTANCE;
	}

	public Path getDataFolder() {
		return configDir;
	}

	public Logger getLogger() {
		return plugin.getLogger();
	}
}
