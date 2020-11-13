package com.elikill58.ipmanager.spigot.impl.item;

import java.util.HashMap;
import java.util.StringJoiner;

import org.bukkit.Material;

import com.elikill58.ipmanager.api.item.ItemRegistrar;
import com.elikill58.ipmanager.spigot.SpigotIpManager;

public class SpigotItemRegistrar extends ItemRegistrar {

	private final HashMap<String, com.elikill58.ipmanager.api.item.Material> cache = new HashMap<>();

	@Override
	public synchronized com.elikill58.ipmanager.api.item.Material get(String id, String... alias) {
		return cache.computeIfAbsent(id, key -> new SpigotMaterial(getMaterial(key, alias)));
	}
	
	private org.bukkit.Material getMaterial(String id, String... alias){
		Material idM = get(id);
		if(idM != null)
			return idM;
		for(String s : alias) {
			Material m = get(s);
			if(m != null)
				return m;
		}
		/*org.bukkit.Material type = org.bukkit.Material.getMaterial(id.toUpperCase());
		if(type != null)
			return type;
		for(String s : alias) {
			type = org.bukkit.Material.getMaterial(s.toUpperCase());
			if(type != null)
				return type;
		}*/
		StringJoiner sj = new StringJoiner(", ", " : ", "");
		for(String tempAlias : alias) sj.add(tempAlias);
		SpigotIpManager.getInstance().getLogger().info("[SpigotItemRegistrar] Cannot find material " + id + sj.toString());
		return null;
	}
	
	private Material get(String name) {
		try {
			return (Material) Material.class.getField(name.toUpperCase()).get(Material.class);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e2) {
			e2.printStackTrace();
		} catch (NoSuchFieldException e) {}
		return null;
	}
}
