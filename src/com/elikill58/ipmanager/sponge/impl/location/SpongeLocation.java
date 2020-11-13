package com.elikill58.ipmanager.sponge.impl.location;

import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.elikill58.ipmanager.api.block.Block;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.sponge.impl.block.SpongeBlock;

public class SpongeLocation extends Location {

	private final org.spongepowered.api.world.Location<World> loc;
	
	public SpongeLocation(org.spongepowered.api.world.Location<World> location) {
		super(new SpongeWorld(location.getExtent()), location.getX(), location.getY(), location.getZ());
		this.loc = location;
	}
	
	public SpongeLocation(com.elikill58.ipmanager.api.location.World w, double x, double y, double z) {
		super(w, x, y, z);
		this.loc = new org.spongepowered.api.world.Location<World>((World) w.getDefault(), x, y, z);
	}

	@Override
	public Block getBlock() {
		return new SpongeBlock(new org.spongepowered.api.world.Location<Extent>(loc.getExtent(), getX(), getY(), getZ()).getBlock().snapshotFor(loc));
	}

	@Override
	public Object getDefault() {
		return new org.spongepowered.api.world.Location<Extent>(loc.getExtent(), getX(), getY(), getZ());
	}
}
