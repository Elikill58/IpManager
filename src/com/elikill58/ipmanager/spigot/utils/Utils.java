package com.elikill58.ipmanager.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ipmanager.spigot.SpigotIpManager;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;
import com.google.common.base.Preconditions;

public class Utils {

	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	public static List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		try {
			Class<?> mcServer = Class.forName("net.minecraft.server." + VERSION + ".MinecraftServer");
			Object server = mcServer.getMethod("getServer").invoke(mcServer);
			Object craftServer = server.getClass().getField("server").get(server);
			Object getted = craftServer.getClass().getMethod("getOnlinePlayers").invoke(craftServer);
			if (getted instanceof Player[])
				for (Player obj : (Player[]) getted)
					list.add(obj);
			else if (getted instanceof List)
				for (Object obj : (List<?>) getted)
					list.add((Player) obj);
			else
				System.out.println("Unknow getOnlinePlayers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ItemStack getItemFromString(String s) {
		Preconditions.checkNotNull(s, "Error while creating item. The material is null.");
		try {
			String[] splitted = s.toUpperCase().split(":");
			String key = splitted[0];
			Material temp = null;
			try {
				temp = Material.valueOf(key);
			} catch (IllegalArgumentException e) {}
			if(temp == null && UniversalUtils.isInteger(key)) {
				try {
					temp = (Material) Material.class.getDeclaredMethod("getMaterial", int.class).invoke(null, Integer.parseInt(key));
				} catch (Exception e) {
					// method not found because of too recent version
				}
			}
			if(temp == null) {
				SpigotIpManager.getInstance().getLogger().warning("Error while creating item. Cannot find item for " + s + ".");
				return null;
			}
			return splitted.length > 1 ? new ItemStack(temp, 1, Byte.parseByte(s.split(":")[1])) : new ItemStack(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
