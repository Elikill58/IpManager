package com.elikill58.ipmanager.common.inventories.holders;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.NegativityHolder;

public class ReportHolder extends NegativityHolder {

	private final Player cible;
	
	public ReportHolder(Player cible) {
		this.cible = cible;
	}

	public Player getCible() {
		return cible;
	}
	
}
