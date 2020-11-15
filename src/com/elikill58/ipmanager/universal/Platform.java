package com.elikill58.ipmanager.universal;

public enum Platform {
	
	BUNGEE("bungee", false),
	SPIGOT("spigot", true),
	SPONGE("sponge", true),
	VELOCITY("velocity", false);
	
	private final String name;
	private final boolean canUseItem;
	
	private Platform(String name, boolean canUseItem) {
		this.name = name;
		this.canUseItem = canUseItem;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean canUseItem() {
		return canUseItem;
	}
}
