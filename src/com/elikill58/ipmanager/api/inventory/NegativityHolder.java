package com.elikill58.ipmanager.api.inventory;

public abstract class NegativityHolder extends InventoryHolder {

	@Override
	public NegativityHolder getBasicHolder() {
		return this;
	}
}
