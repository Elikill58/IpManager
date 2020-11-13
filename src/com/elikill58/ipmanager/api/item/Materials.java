package com.elikill58.ipmanager.api.item;

public class Materials {

	public static final Material AIR = ItemRegistrar.getInstance().get("air");

	public static final Material BARRIER = ItemRegistrar.getInstance().get("barrier", "redstone"); // redstone for 1.7

	public static final Material PLAYER_HEAD = ItemRegistrar.getInstance().get("player_head", "skull_item", "skull");
}
