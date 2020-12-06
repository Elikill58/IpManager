package com.elikill58.ipmanager.common.inventories;

import static com.elikill58.ipmanager.universal.Messages.getMessage;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.inventory.InventoryClickEvent;
import com.elikill58.ipmanager.api.inventory.AbstractInventory;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.InventoryManager;
import com.elikill58.ipmanager.api.item.ItemBuilder;
import com.elikill58.ipmanager.api.item.ItemStack;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.item.Materials;
import com.elikill58.ipmanager.common.inventories.holders.IpPlayerHolder;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Messages;
import com.elikill58.ipmanager.universal.IP.IpInfos;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;

public class IpPlayerInventory extends AbstractInventory<IpPlayerHolder> {
	
	public static final ItemStack EMPTY = ItemBuilder.Builder(Materials.GRAY_STAINED_GLASS_PANE).displayName(" - ").build();
	
	public IpPlayerInventory() {
		super(NegativityInventory.IP_PLAYER, IpPlayerHolder.class);
	}

	@Override
	public void openInventory(Player p, Object... args) {
		OfflinePlayer cible = (OfflinePlayer) args[0];
		IpPlayerAccount acc = IpPlayerAccountManager.getManager().getNow(cible.getUniqueId());
		Inventory inv = Adapter.getAdapter().createInventory(getMessage("inv.player.name", "%cible%", cible.getName()), 18, new IpPlayerHolder(cible));
		
		String[] placeholders = new String[] { "%basic%", acc.getBasicIp(), "%proxy%", acc.getProxy() == null ? "Any" : acc.getProxy(), "%fai%", acc.getFai(), "%ping%", "" + p.getPing() };
		inv.set(0, EMPTY);
		inv.set(1, EMPTY);
		inv.set(2, ItemBuilder.Builder(Materials.DIRT).displayName(getMessage("inv.player.basicip", placeholders)).build());
		inv.set(3, ItemBuilder.Builder(Materials.GRASS).displayName(getMessage("inv.player.proxy", placeholders)).build());
		inv.set(4, ItemBuilder.Builder(Materials.STONE).displayName(getMessage("inv.player.fai", placeholders)).build());
		inv.set(5, EMPTY);
		inv.set(6, EMPTY);
		inv.set(7, ItemBuilder.Builder(Materials.ARROW).displayName(getMessage("inv.player.ping", placeholders)).build());
		inv.set(8, cible instanceof Player ? ItemBuilder.getSkullItem((Player) cible) :
					ItemBuilder.Builder(Materials.PLAYER_HEAD).displayName(cible.getName() + " (offline)").build());
		inv.set(9, EMPTY);
		
		List<String> infosPlaceholdersList = new ArrayList<>();
		for(IpInfos infos : IpInfos.values()) {
			infosPlaceholdersList.add("%" + infos.name().toLowerCase() + "%");
			infosPlaceholdersList.add(acc.getIP().getIpInfos(infos));
		}
		String[] infosPlaceholders = infosPlaceholdersList.toArray(new String[] {});
		for(int i = 1; i < 6; i++)
			inv.set(9 + i, ItemBuilder.Builder(Materials.APPLE).displayName(getMessage("inv.player.sub" + i + ".name", infosPlaceholders))
					.lore(Messages.getStringList("inv.player.sub" + i + ".lore", infosPlaceholders)).build());
		
		inv.set(15, EMPTY);
		inv.set(16, ItemBuilder.Builder(Materials.BOOK_AND_QUILL).displayName(getMessage("inv.player.connections")).build());
		inv.set(inv.getSize() - 1, ItemBuilder.Builder(Materials.BARRIER).displayName(getMessage("inv.close")).build());
		p.openInventory(inv);
	}

	@Override
	public void manageInventory(InventoryClickEvent e, Material m, Player p, IpPlayerHolder nh) {
		if(m.equals(Materials.BOOK_AND_QUILL)) {
			InventoryManager.open(NegativityInventory.CONNECTIONS_TIMES, p, nh.getCible());
		}
	}
}
