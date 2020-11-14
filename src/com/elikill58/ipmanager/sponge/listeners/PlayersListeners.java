package com.elikill58.ipmanager.sponge.listeners;

import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.elikill58.ipmanager.api.PlayerAddress;
import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.api.events.player.PlayerChatEvent;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerDeathEvent;
import com.elikill58.ipmanager.api.events.player.PlayerInteractEvent;
import com.elikill58.ipmanager.api.events.player.PlayerInteractEvent.Action;
import com.elikill58.ipmanager.api.events.player.PlayerItemConsumeEvent;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.api.events.player.PlayerMoveEvent;
import com.elikill58.ipmanager.api.events.player.PlayerTeleportEvent;
import com.elikill58.ipmanager.sponge.impl.entity.SpongeEntityManager;
import com.elikill58.ipmanager.sponge.impl.entity.SpongePlayer;
import com.elikill58.ipmanager.sponge.impl.item.SpongeItemStack;
import com.elikill58.ipmanager.sponge.impl.location.SpongeLocation;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;

public class PlayersListeners {

	@Listener
	public void onPreLogin(ClientConnectionEvent.Login e) {
		GameProfile profil = e.getProfile();
		RemoteConnection co = e.getConnection();
		LoginEvent event = new LoginEvent(IpPlayerAccountManager.getManager().getNow(profil.getUniqueId()),
				profil.getUniqueId(), profil.getName().orElse(null),
				e.isCancelled() ? Result.KICK_BANNED : Result.ALLOWED, new PlayerAddress(co.getAddress()),
				new PlayerAddress(co.getVirtualHost()), e.getMessage().toPlain());
		EventManager.callEvent(event);
		e.setMessage(Text.of(event.getKickMessage()));
		e.setCancelled(!event.getLoginResult().equals(Result.ALLOWED));
	}

	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join e, @First Player p) {
		PlayerConnectEvent event = new PlayerConnectEvent(Players.getPlayer(p.getUniqueId(), () -> new SpongePlayer(p)),
				IpPlayerAccountManager.getManager().getNow(p.getUniqueId()), e.getMessage().toPlain());
		EventManager.callEvent(event);
		e.setMessage(Text.of(event.getJoinMessage()));
	}

	@Listener
	public void onLeave(ClientConnectionEvent.Disconnect e, @First Player p) {
		PlayerLeaveEvent event = new PlayerLeaveEvent(Players.getPlayer(p.getUniqueId(), () -> new SpongePlayer(p)),
				IpPlayerAccountManager.getManager().getNow(p.getUniqueId()), e.getMessage().toPlain());
		EventManager.callEvent(event);
		e.setMessage(Text.of(event.getQuitMessage()));
	}

	@SuppressWarnings("unchecked")
	@Listener
	public void onPlayerMove(MoveEntityEvent e, @First Player p) {
		Transform<World> to = e.getToTransform();
		PlayerMoveEvent event = new PlayerMoveEvent(SpongeEntityManager.getPlayer(p),
				new SpongeLocation(e.getFromTransform().getLocation()), new SpongeLocation(to.getLocation()));
		EventManager.callEvent(event);
		if (event.hasToSet()) {
			to.setLocation((Location<World>) event.getTo().getDefault());
			e.setToTransform(to);
		}
	}

	@Listener
	public void onTeleport(MoveEntityEvent.Teleport e, @First Player p) {
		EventManager.callEvent(new PlayerTeleportEvent(SpongeEntityManager.getPlayer(p),
				new SpongeLocation(e.getFromTransform().getLocation()),
				new SpongeLocation(e.getToTransform().getLocation())));
	}

	@Listener
	public void onChat(MessageChannelEvent.Chat e, @First Player p) {
		PlayerChatEvent event = new PlayerChatEvent(SpongeEntityManager.getPlayer(p), e.getMessage().toPlain(),
				e.getFormatter().toText().toPlain());
		EventManager.callEvent(event);
		e.setCancelled(event.isCancelled());
	}

	@Listener
	public void onDeath(DestructEntityEvent.Death e, @First Player p) {
		EventManager.callEvent(new PlayerDeathEvent(SpongeEntityManager.getPlayer(p)));
	}

	@Listener
	public void onInteract(InteractEvent e, @First Player p) {
		PlayerInteractEvent event = new PlayerInteractEvent(SpongeEntityManager.getPlayer(p), Action.LEFT_CLICK_AIR);
		EventManager.callEvent(event);
		e.setCancelled(event.isCancelled());
	}

	@Listener
	public void onItemConsume(UseItemStackEvent.Finish e, @First Player p) {
		PlayerItemConsumeEvent event = new PlayerItemConsumeEvent(SpongeEntityManager.getPlayer(p),
				new SpongeItemStack(e.getItemStackInUse().createStack()));
		EventManager.callEvent(event);
		e.setCancelled(event.isCancelled());
	}
}
