package com.elikill58.ipmanager.spigot.impl.block;

import com.elikill58.ipmanager.api.block.Block;
import com.elikill58.ipmanager.api.block.BlockFace;
import com.elikill58.ipmanager.api.item.ItemRegistrar;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.spigot.impl.location.SpigotLocation;

public class SpigotBlock extends Block {

	private final org.bukkit.block.Block block;

	public SpigotBlock(org.bukkit.block.Block block) {
		this.block = block;
	}

	@Override
	public Material getType() {
		return ItemRegistrar.getInstance().get(block.getType().name());
	}

	@Override
	public int getX() {
		return block.getX();
	}

	@Override
	public int getY() {
		return block.getY();
	}

	@Override
	public int getZ() {
		return block.getZ();
	}

	@Override
	public Block getRelative(BlockFace blockFace) {
		return new SpigotBlock(block.getRelative(org.bukkit.block.BlockFace.valueOf(blockFace.name())));
	}

	@Override
	public Location getLocation() {
		return new SpigotLocation(block.getLocation());
	}

	@Override
	public void setType(Material type) {
		block.setType((org.bukkit.Material) type.getDefault());
	}

	@Override
	public Object getDefault() {
		return block;
	}
}
