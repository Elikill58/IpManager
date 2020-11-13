package com.elikill58.ipmanager.spigot.impl.item;

import com.elikill58.ipmanager.api.item.Material;

public class SpigotMaterial extends Material {

	private final org.bukkit.Material type;
	
	public SpigotMaterial(org.bukkit.Material type) {
		this.type = type;
	}
	
	@Override
	public boolean isSolid() {
		return type.isSolid();
	}

	@Override
	public String getId() {
		return type.name();
	}

	@Override
	public boolean isTransparent() {
		return type.isTransparent();
	}

	@Override
	public Object getDefault() {
		return type;
	}
}
