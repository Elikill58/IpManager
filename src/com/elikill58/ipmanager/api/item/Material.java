package com.elikill58.ipmanager.api.item;

import com.elikill58.ipmanager.api.IpManagerObject;

public abstract class Material extends IpManagerObject {

	/**
	 * Check if the material can be solid
	 * 
	 * @return true if it's solid
	 */
	public abstract boolean isSolid();

	/**
	 * Check if the type is transparent
	 * (if we can see through it)
	 * 
	 * @return true if it is
	 */
	public abstract boolean isTransparent();

	/**
	 * Get the material main ID
	 * (the official platform's name)
	 * 
	 * @return the material type name
	 */
	public abstract String getId();

	/**
	 * Check if an item is consumable
	 * Warn: it's a manual check, on a generic one for all material.
	 * An item are return false but be consumable.
	 * 
	 * @return true if can be ate
	 */
	public boolean isConsumable() {
		String id = getId().toLowerCase();
		return id.contains("cooked") || id.contains("mutton") || id.contains("beef") || id.contains("apple")
				|| id.contains("potato") || id.contains("carrot") || id.contains("bread") || id.contains("chicken")
				|| id.contains("salmon") || id.contains("rabbit") || id.contains("porkshop") || id.contains("fish");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Material))
			return false;
		Material to = (Material) obj;
		if (this.getId().equals(to.getId()) && this.isSolid() == to.isSolid()
				&& this.isTransparent() == to.isTransparent())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Material{id=" + getId() + "}";
	}
}
