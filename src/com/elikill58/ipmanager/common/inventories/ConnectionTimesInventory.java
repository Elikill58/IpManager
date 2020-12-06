package com.elikill58.ipmanager.common.inventories;

import static com.elikill58.ipmanager.universal.Messages.getMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.elikill58.ipmanager.api.entity.OfflinePlayer;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.inventory.InventoryClickEvent;
import com.elikill58.ipmanager.api.inventory.AbstractInventory;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.item.ItemBuilder;
import com.elikill58.ipmanager.api.item.ItemStack;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.item.Materials;
import com.elikill58.ipmanager.common.inventories.holders.ConnectionTimesHolder;
import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;

public class ConnectionTimesInventory extends AbstractInventory<ConnectionTimesHolder> {

	public static final ItemStack EMPTY = ItemBuilder.Builder(Materials.GRAY_STAINED_GLASS_PANE).displayName(" - ")
			.build();
	private static final SimpleDateFormat DAY_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
	private static final SimpleDateFormat FULL_DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss");

	public ConnectionTimesInventory() {
		super(NegativityInventory.CONNECTIONS_TIMES, ConnectionTimesHolder.class);
	}

	@Override
	public void openInventory(Player p, Object... args) {
		OfflinePlayer cible = (OfflinePlayer) args[0];
		IpPlayerAccount acc = IpPlayerAccountManager.getManager().getNow(cible.getUniqueId());
		HashMap<String, List<Long>> timesPerDays = new HashMap<>();
		acc.getAllConnections().forEach((time) -> {
			String timeDay = DAY_FORMATTER.format(new Date(time));
			timesPerDays.computeIfAbsent(timeDay, (s) -> new ArrayList<>()).add(time);
		});
		int amountOfDays = timesPerDays.size();
		int page = args.length == 1 ? 0 : (int) args[1];
		int size = amountOfDays - (page * 45) + 1;

		Inventory inv = Adapter.getAdapter().createInventory(
				getMessage("inv.player.connections", "%cible%", cible.getName()),
				UniversalUtils.getMultipleOf(size, 9, 1, 54), new ConnectionTimesHolder(cible));
		int slot = 0, beginIndex = page * 45, i;
		for (i = beginIndex; i < (beginIndex + size - 1); i++) {
			List<Long> list = timesPerDays.get(new ArrayList<>(timesPerDays.keySet()).get(i));
			if (list == null)
				break;
			List<String> lore = new ArrayList<>();
			list.forEach((l) -> {
				lore.add(getMessage("inv.connections.lore_line", "%date%", FULL_DATE_FORMATTER.format(new Date(l))));
			});
			inv.set(slot++, ItemBuilder.Builder(Materials.BOOK).displayName(
					getMessage("inv.connections.co_name", "%date%", DAY_FORMATTER.format(new Date(list.get(0)))))
					.lore(lore).build());
		}
		if(page > 0)
			inv.set(inv.getSize() - 3, ItemBuilder.Builder(Materials.ARROW).displayName("Back").build());
		if(i < amountOfDays)
			inv.set(inv.getSize() - 2, ItemBuilder.Builder(Materials.ARROW).displayName("Next").build());
		inv.set(inv.getSize() - 1, ItemBuilder.Builder(Materials.BARRIER).displayName(getMessage("inv.close")).build());
		p.openInventory(inv);
	}

	@Override
	public void manageInventory(InventoryClickEvent e, Material m, Player p, ConnectionTimesHolder nh) {

	}
}
