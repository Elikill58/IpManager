package com.elikill58.ipmanager.common.inventories.holders;

import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.inventory.NegativityHolder;

public class IpPlayerHolder extends NegativityHolder {

	private final OfflinePlayer cible;
	
	public IpPlayerHolder(OfflinePlayer cible) {
		this.cible = cible;
	}

	public OfflinePlayer getCible() {
		return cible;
	}
	
}
