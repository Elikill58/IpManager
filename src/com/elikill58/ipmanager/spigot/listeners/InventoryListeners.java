package com.elikill58.ipmanager.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.inventory.InventoryAction;
import com.elikill58.ipmanager.api.events.inventory.InventoryClickEvent;
import com.elikill58.ipmanager.api.events.inventory.InventoryOpenEvent;
import com.elikill58.ipmanager.api.item.ItemStack;
import com.elikill58.ipmanager.spigot.impl.entity.SpigotEntityManager;
import com.elikill58.ipmanager.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ipmanager.spigot.impl.item.SpigotItemStack;

public class InventoryListeners implements Listener {
	
	@EventHandler
	public void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent e) {
		InventoryOpenEvent event = new InventoryOpenEvent(SpigotEntityManager.getPlayer((Player) e.getPlayer()));
		EventManager.callEvent(event);
		if(event.isCancelled())
			e.setCancelled(event.isCancelled());
	}
	
	@EventHandler
	public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || e.getCurrentItem() == null)
			return;
		com.elikill58.ipmanager.api.entity.Player p = SpigotEntityManager.getPlayer((Player) e.getWhoClicked());
		InventoryAction action = getAction(e.getClick());
		ItemStack item = new SpigotItemStack(e.getCurrentItem());
		InventoryClickEvent event = new InventoryClickEvent(p, action, e.getSlot(), item, new SpigotInventory(e.getClickedInventory()));
		EventManager.callEvent(event);
		if(event.isCancelled())
			e.setCancelled(event.isCancelled());
	}
	
	private InventoryAction getAction(ClickType type) {
		switch (type) {
		case CONTROL_DROP:
		case DROP:
			return InventoryAction.DROP;
		case CREATIVE:
			return InventoryAction.CREATIVE;
		case DOUBLE_CLICK:
			return InventoryAction.DOUBLE;
		case LEFT:
		case WINDOW_BORDER_LEFT:
			return InventoryAction.LEFT;
		case SHIFT_LEFT:
			return InventoryAction.LEFT_SHIFT;
		case MIDDLE:
			return InventoryAction.MIDDLE;
		case RIGHT:
		case WINDOW_BORDER_RIGHT:
			return InventoryAction.RIGHT;
		case SHIFT_RIGHT:
			return InventoryAction.RIGHT_SHIFT;
		case NUMBER_KEY:
			return InventoryAction.NUMBER;
		case UNKNOWN:
			return InventoryAction.UNKNOWN;
		}
		return InventoryAction.UNKNOWN;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		com.elikill58.ipmanager.api.entity.Player p = SpigotEntityManager.getPlayer((Player) e.getPlayer());
		EventManager.callEvent(new com.elikill58.ipmanager.api.events.inventory.InventoryCloseEvent(p, new SpigotInventory(e.getInventory())));
	}
}
