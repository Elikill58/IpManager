package com.elikill58.ipmanager.sponge.impl.inventory;

import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

import com.elikill58.ipmanager.api.inventory.PlatformHolder;

public class SpongeNegativityHolder extends PlatformHolder implements Carrier {

	private final PlatformHolder holder;
	
	public SpongeNegativityHolder(PlatformHolder holder) {
		this.holder = holder;
	}

	@Override
	public PlatformHolder getBasicHolder() {
		return holder;
	}

	@Override
	public CarriedInventory<? extends Carrier> getInventory() {
		return null;
	}
}
