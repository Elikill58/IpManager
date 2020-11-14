package com.elikill58.ipmanager.sponge.impl.entity;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import com.elikill58.ipmanager.api.GameMode;
import com.elikill58.ipmanager.api.PlayerAddress;
import com.elikill58.ipmanager.api.entity.Entity;
import com.elikill58.ipmanager.api.entity.EntityType;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.inventory.Inventory;
import com.elikill58.ipmanager.api.inventory.PlayerInventory;
import com.elikill58.ipmanager.api.location.Location;
import com.elikill58.ipmanager.api.location.World;
import com.elikill58.ipmanager.sponge.SpongeIpManager;
import com.elikill58.ipmanager.sponge.impl.inventory.SpongeInventory;
import com.elikill58.ipmanager.sponge.impl.inventory.SpongePlayerInventory;
import com.elikill58.ipmanager.sponge.impl.location.SpongeLocation;
import com.elikill58.ipmanager.sponge.impl.location.SpongeWorld;

public class SpongePlayer extends Player {

	private final org.spongepowered.api.entity.living.player.Player p;

	public SpongePlayer(org.spongepowered.api.entity.living.player.Player p) {
		this.p = p;
	}

	@Override
	public UUID getUniqueId() {
		return p.getUniqueId();
	}

	@Override
	public void sendMessage(String msg) {
		p.sendMessage(Text.of(msg));
	}

	@Override
	public boolean isOnGround() {
		return p.isOnGround();
	}

	@Override
	public boolean isOp() {
		return p.hasPermission("*");
	}

	@Override
	public GameMode getGameMode() {
		return GameMode.get(p.gameMode().get().getName());
	}
	
	@Override
	public void setGameMode(GameMode gameMode) {
		switch (gameMode) {
		case ADVENTURE:
			p.gameMode().set(GameModes.ADVENTURE);
			break;
		case CREATIVE:
			p.gameMode().set(GameModes.CREATIVE);
			break;
		case CUSTOM:
			p.gameMode().set(GameModes.NOT_SET);
			break;
		case SPECTATOR:
			p.gameMode().set(GameModes.SPECTATOR);
			break;
		case SURVIVAL:
			p.gameMode().set(GameModes.SURVIVAL);
			break;
		}
	}

	@Override
	public Location getLocation() {
		return new SpongeLocation(p.getLocation());
	}

	@Override
	public int getPing() {
		return p.getConnection().getLatency();
	}

	@Override
	public World getWorld() {
		return new SpongeWorld(p.getWorld());
	}

	@Override
	public String getName() {
		return p.getName();
	}

	@Override
	public boolean hasPermission(String perm) {
		return p.hasPermission(perm);
	}

	@Override
	public void kick(String reason) {
		p.kick(Text.of(reason));
	}

	@Override
	public PlayerAddress getIP() {
		return new PlayerAddress(p.getConnection().getAddress());
	}

	@Override
	public boolean isOnline() {
		return p.isOnline();
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public void teleport(Entity et) {
		teleport(et.getLocation());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void teleport(Location loc) {
		p.setLocation((org.spongepowered.api.world.Location<org.spongepowered.api.world.World>) loc.getDefault());
	}

	@Override
	public boolean isDead() {
		return p.getHealthData().health().get() <= 0;
	}

	@Override
	public PlayerInventory getInventory() {
		return new SpongePlayerInventory(p, p.getInventory());
	}
	
	@Override
	public boolean hasOpenInventory() {
		return p.getOpenInventory().isPresent() && p.getOpenInventory().get().getArchetype().equals(InventoryArchetypes.CHEST);
	}

	@Override
	public Inventory getOpenInventory() {
		return p.getOpenInventory().isPresent() ? null : new SpongeInventory(p.getOpenInventory().get());
	}

	@Override
	public void openInventory(Inventory inv) {
		p.openInventory((org.spongepowered.api.item.inventory.Inventory) inv.getDefault());
	}

	@Override
	public void closeInventory() {
		Task.builder().execute(() -> p.closeInventory()).submit(SpongeIpManager.getInstance());
	}

	@Override
	public void updateInventory() {
		
	}

	@Override
	public Object getDefault() {
		return p;
	}
	
	@Override
	public void performCommand(String cmd) {
		
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return p.getConnection().getAddress();
	}
}
