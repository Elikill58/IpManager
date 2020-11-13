package com.elikill58.ipmanager.common.inventories;

import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.inventory.InventoryClickEvent;
import com.elikill58.ipmanager.api.inventory.AbstractInventory;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.common.inventories.holders.ReportHolder;

public class ReportInventory extends AbstractInventory<ReportHolder> {
	
	public ReportInventory() {
		super(NegativityInventory.REPORT, ReportHolder.class);
	}
	
	@Override
	public void load() {

	}

	@Override
	public void openInventory(Player p, Object... args) {
		
	}

	@Override
	public void manageInventory(InventoryClickEvent e, Material m, Player p, ReportHolder nh) {
	}
}
