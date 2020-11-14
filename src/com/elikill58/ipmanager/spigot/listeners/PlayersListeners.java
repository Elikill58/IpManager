package com.elikill58.ipmanager.spigot.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.api.events.player.PlayerChatEvent;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerDamageByEntityEvent;
import com.elikill58.ipmanager.api.events.player.PlayerInteractEvent;
import com.elikill58.ipmanager.api.events.player.PlayerInteractEvent.Action;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.api.events.player.PlayerMoveEvent;
import com.elikill58.ipmanager.api.events.player.PlayerRegainHealthEvent;
import com.elikill58.ipmanager.api.events.player.PlayerTeleportEvent;
import com.elikill58.ipmanager.spigot.SpigotIpManager;
import com.elikill58.ipmanager.spigot.impl.entity.SpigotEntityManager;
import com.elikill58.ipmanager.spigot.impl.entity.SpigotPlayer;
import com.elikill58.ipmanager.spigot.impl.item.SpigotItemStack;
import com.elikill58.ipmanager.spigot.impl.location.SpigotLocation;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;

public class PlayersListeners implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		IpPlayerAccount np = IpPlayerAccountManager.getManager().getNow(p.getUniqueId());
		PlayerLeaveEvent event = new PlayerLeaveEvent(Players.getPlayer(p.getUniqueId(), () -> new SpigotPlayer(p)), np, e.getQuitMessage());
		EventManager.callEvent(event);
		e.setQuitMessage(event.getQuitMessage());
	}
	
	@EventHandler
	public void onMove(org.bukkit.event.player.PlayerMoveEvent e) {
		Player p = e.getPlayer();
		PlayerMoveEvent event = new PlayerMoveEvent(SpigotEntityManager.getPlayer(p), new SpigotLocation(e.getFrom()), new SpigotLocation(e.getTo()));
		Bukkit.getScheduler().runTaskAsynchronously(SpigotIpManager.getInstance(), () -> EventManager.callEvent(event));
		if(event.hasToSet()) {
			e.setTo((Location) event.getTo().getDefault());
			e.setFrom((Location) event.getFrom().getDefault());
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Bukkit.getScheduler().runTask(SpigotIpManager.getInstance(), () -> {
			PlayerChatEvent event = new PlayerChatEvent(SpigotEntityManager.getPlayer(e.getPlayer()), e.getMessage(), e.getFormat());
			EventManager.callEvent(event);
			if(event.isCancelled())
				e.setCancelled(event.isCancelled());
		});
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player)
			EventManager.callEvent(new PlayerDamageByEntityEvent(SpigotEntityManager.getPlayer((Player) e.getEntity()), SpigotEntityManager.getEntity(e.getDamager())));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		EventManager.callEvent(new com.elikill58.ipmanager.api.events.player.PlayerDeathEvent(SpigotEntityManager.getPlayer(e.getEntity())));
	}
	
	@EventHandler
	public void onInteract(org.bukkit.event.player.PlayerInteractEvent e) {
		PlayerInteractEvent event = new PlayerInteractEvent(SpigotEntityManager.getPlayer(e.getPlayer()), Action.valueOf(e.getAction().name()));
		EventManager.callEvent(event);
		if(event.isCancelled())
			e.setCancelled(event.isCancelled());
	}
	
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent e) {
		EventManager.callEvent(new com.elikill58.ipmanager.api.events.player.PlayerItemConsumeEvent(SpigotEntityManager.getPlayer(e.getPlayer()), new SpigotItemStack(e.getItem())));
	}
	
	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent e) {
		if(e.getEntity() instanceof Player) {
			PlayerRegainHealthEvent event = new PlayerRegainHealthEvent(SpigotEntityManager.getPlayer((Player) e.getEntity()));
			EventManager.callEvent(event);
			if(event.isCancelled())
				e.setCancelled(event.isCancelled());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		LoginEvent event = new LoginEvent(IpPlayerAccountManager.getManager().getNow(p.getUniqueId()), uuid, p.getName(), Result.valueOf(e.getResult().name()), e.getAddress(), e.getRealAddress(), e.getKickMessage());
		EventManager.callEvent(event);
		e.setKickMessage(event.getKickMessage());
		e.setResult(PlayerLoginEvent.Result.valueOf(event.getLoginResult().name()));
	}
	
	@EventHandler
	public void onTeleport(org.bukkit.event.player.PlayerTeleportEvent e) {
		EventManager.callEvent(new PlayerTeleportEvent(SpigotEntityManager.getPlayer(e.getPlayer()), new SpigotLocation(e.getFrom()), new SpigotLocation(e.getTo())));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		IpPlayerAccount np = IpPlayerAccountManager.getManager().getNow(p.getUniqueId());
		PlayerConnectEvent event = new PlayerConnectEvent(Players.getPlayer(p.getUniqueId(), () -> new SpigotPlayer(p)), np, e.getJoinMessage());
		EventManager.callEvent(event);
		e.setJoinMessage(event.getJoinMessage());
	}
}
