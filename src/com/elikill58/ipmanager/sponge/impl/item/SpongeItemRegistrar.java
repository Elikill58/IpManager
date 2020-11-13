package com.elikill58.ipmanager.sponge.impl.item;

import java.util.HashMap;
import java.util.Optional;
import java.util.StringJoiner;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;

import com.elikill58.ipmanager.api.item.ItemRegistrar;
import com.elikill58.ipmanager.api.item.Material;
import com.elikill58.ipmanager.sponge.SpongeIpManager;

public class SpongeItemRegistrar extends ItemRegistrar {

	private final HashMap<String, Material> cache = new HashMap<>();

	@Override
	public Material get(String id, String... alias) {
		return cache.computeIfAbsent(id, key -> {
			Optional<ItemType> optId = Sponge.getRegistry().getType(ItemType.class, parse(id));
			if(optId.isPresent())
				return new SpongeMaterial(optId.get());
			Optional<BlockType> optBlock = Sponge.getRegistry().getType(BlockType.class, parse(id));
			if(optBlock.isPresent() && optBlock.get().getItem().isPresent())
				return new SpongeMaterial(optBlock.get().getItem().get());
			for(String tempID : alias) {
				Optional<ItemType> optAlias = Sponge.getRegistry().getType(ItemType.class, parse(tempID));
				if(optAlias.isPresent())
					return new SpongeMaterial(optAlias.get());
			}
			StringJoiner sj = new StringJoiner(", ", " : ", "");
			for(String tempAlias : alias) sj.add(tempAlias + " (" + parse(tempAlias) + ")");
			SpongeIpManager.getInstance().getLogger().info("[SpongeItemRegistrar] Cannot find material " + id + sj.toString());
			return null;
		});
	}
	
	private String parse(String base) {
		StringJoiner sj = new StringJoiner("");
		for(String ch : base.split("")) {
			if(ch.equals("["))
				return sj.toString();
			else
				sj.add(ch);
		}
		return sj.toString();
	}
}
