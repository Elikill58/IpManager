package com.elikill58.ipmanager.spigot.impl.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.elikill58.ipmanager.api.colors.ChatColor;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.item.Enchantment;
import com.elikill58.ipmanager.api.item.ItemBuilder;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.api.item.Materials;
import com.elikill58.ipmanager.api.utils.Utils;

public class SpigotItemBuilder extends ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public SpigotItemBuilder(Material type) {
    	this.itemStack = new ItemStack((org.bukkit.Material) type.getDefault());
    	this.itemMeta = (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

    public SpigotItemBuilder(String material) {
    	this.itemStack = com.elikill58.ipmanager.spigot.utils.Utils.getItemFromString(material);
    	this.itemMeta = (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

	public SpigotItemBuilder(Player owner) {
    	this.itemStack = new ItemStack((org.bukkit.Material) Materials.PLAYER_HEAD.getDefault(), 1, (byte) 3);
    	SkullMeta skullmeta = (SkullMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
		skullmeta.setOwner(owner.getName());
		this.itemMeta = skullmeta;
	}

	@Override
    public ItemBuilder displayName(@Nullable String displayName) {
        this.itemMeta.setDisplayName(ChatColor.RESET + Utils.coloredMessage(displayName));
        return this;
    }

    @Override
    public ItemBuilder resetDisplayName() {
        return this.displayName(null);
    }

    @Override
	public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.name()), level, true);
        return this;
    }

    @Override
    public ItemBuilder unsafeEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment.name()), level, true);
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder lore(List<String> lore) {
    	return lore(lore.toArray(new String[] {}));
    }

    @Override
    public ItemBuilder lore(String... lore) {
        List<String> list = this.itemMeta.hasLore() ? this.itemMeta.getLore() : new ArrayList<>();
    	for(String s : lore)
    		for(String temp : s.split("\\n"))
        		for(String tt : temp.split("/n"))
        			list.add(Utils.coloredMessage(tt));
        this.itemMeta.setLore(list);
        return this;
    }

    @Override
    public ItemBuilder addToLore(String... loreToAdd) {
        return lore(loreToAdd);
    }

    @Override
    public com.elikill58.ipmanager.api.item.ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return new SpigotItemStack(itemStack);
    }
}
