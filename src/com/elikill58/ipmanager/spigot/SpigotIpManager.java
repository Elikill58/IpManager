package com.elikill58.ipmanager.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.spigot.listeners.BlockListeners;
import com.elikill58.ipmanager.spigot.listeners.CommandsListeners;
import com.elikill58.ipmanager.spigot.listeners.EntityListeners;
import com.elikill58.ipmanager.spigot.listeners.InventoryListeners;
import com.elikill58.ipmanager.spigot.listeners.PlayersListeners;
import com.elikill58.ipmanager.spigot.utils.Utils;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.Version;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;

public class SpigotIpManager extends JavaPlugin {

	private static SpigotIpManager INSTANCE;
	public static boolean isCraftBukkit = false;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		if (Adapter.getAdapter() == null)
			Adapter.setAdapter(new SpigotAdapter(this));
		
		IpManager.init();
		
		Version v = Version.getVersion(Utils.VERSION);
		if (v.equals(Version.HIGHER))
			getLogger().warning("Unknow server version " + Utils.VERSION + " ! Some problems can appears.");
		else
			getLogger().info("Detected server version: " + v.name().toLowerCase() + " (" + Utils.VERSION + ")");
		
		saveDefaultConfig();
		try {
			Class.forName("org.spigotmc.SpigotConfig");
			isCraftBukkit = false;
		} catch (ClassNotFoundException e) {
			isCraftBukkit = true;
		}

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayersListeners(), this);
		pm.registerEvents(new InventoryListeners(), this);
		pm.registerEvents(new BlockListeners(), this);
		pm.registerEvents(new EntityListeners(), this);

		loadCommand();
		
		IpManagerAccountStorage.setDefaultStorage("file");
		
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
			for(Player p : Adapter.getAdapter().getOnlinePlayers()) {
				IpPlayerAccount ip = IpPlayerAccountManager.getManager().getNow(p.getUniqueId());
				ip.loadIP();
				ip.setFai(p.getAddress().getHostName());
				ip.save();
			}
		});
	}

	private void loadCommand() {
		CommandsListeners command = new CommandsListeners();
		PluginCommand negativity = getCommand("getip");
		negativity.setExecutor(command);
		negativity.setTabCompleter(command);
	}

	@Override
	public void onDisable() {
		Database.close();
	}

	public static SpigotIpManager getInstance() {
		return INSTANCE;
	}
}
