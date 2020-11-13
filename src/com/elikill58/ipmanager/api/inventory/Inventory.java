package com.elikill58.ipmanager.api.inventory;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ipmanager.api.IpManagerObject;
import com.elikill58.ipmanager.api.item.ItemStack;
import com.elikill58.ipmanager.universal.Adapter;

public abstract class Inventory extends IpManagerObject {
	
	/**
	 * Get the inventory type
	 * 
	 * @return the inventory type
	 */
	public abstract InventoryType getType();
	
	/**
	 * Get the item in the inventory at the given slot
	 * 
	 * @param slot the slot where we will search the item
	 * @return the item at the given slot
	 */
	public abstract @Nullable ItemStack get(int slot);

	/**
	 * Set a new item to the given slot
	 * 
	 * @param slot the slot of the item
	 * @param item the new item
	 */
	public abstract void set(int slot, ItemStack item);
	
	/**
	 * Remove item at the given slot
	 * (Can also be used if you set a null item at the given slot)
	 * 
	 * @param slot the removed item slot
	 */
	public abstract void remove(int slot);
	
	/**
	 * Remove all content of the inventory
	 */
	public abstract void clear();

	/**
	 * Add an item at the first available slot
	 * 
	 * @param build the new item
	 */
	public abstract void addItem(ItemStack build);
	
	/**
	 * Get the size of the inventory
	 * 
	 * @return the inventory size
	 */
	public abstract int getSize();
	
	/**
	 * Get the inventory name
	 * 
	 * @return the inventory name
	 */
	public abstract String getInventoryName();
	
	/**
	 * Get the platform holder.
	 * The platform holder can be just correspond to a platform one
	 * Or can be an {@link NegativityHolder} if it's a Negativity inventory.
	 * 
	 * @return the inventory holder
	 */
	public abstract PlatformHolder getHolder();
	
	/**
	 * Create an inventory according to the specific platform
	 * 
	 * @param inventoryName the name of the inventory
	 * @param size the size of the inventory
	 * @param holder the negativity holder that will be applied to the inventory
	 * @return the new inventory
	 */
	public static Inventory createInventory(String inventoryName, int size, NegativityHolder holder) {
		return Adapter.getAdapter().createInventory(inventoryName, size, holder);
	}
}
