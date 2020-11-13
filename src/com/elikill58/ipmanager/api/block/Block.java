package com.elikill58.ipmanager.api.block;

import com.elikill58.ipmanager.api.IpManagerObject;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.location.Location;
import com.google.common.base.Preconditions;

public abstract class Block extends IpManagerObject {

	public abstract Location getLocation();
	
	public abstract Material getType();

	public abstract int getX();
	public abstract int getY();
	public abstract int getZ();
	
	public abstract Block getRelative(BlockFace blockFace);

	public abstract void setType(Material type);
	
	@Override
	public boolean equals(Object obj) {
		Preconditions.checkNotNull(obj);
		if(!(obj instanceof Block))
			return false;
		Block b = (Block) obj;
		return b.getLocation().equals(getLocation()) && getType().equals(b.getType());
	}
	
	@Override
	public String toString() {
		return "Block{type=" + getType().getId() + ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "}";
	}
}
