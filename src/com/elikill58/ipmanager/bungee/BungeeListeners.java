package com.elikill58.ipmanager.bungee;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.bungee.impl.entity.BungeePlayer;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListeners implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPreLogin(net.md_5.bungee.api.event.LoginEvent e) {
		PendingConnection co = e.getConnection();
		LoginEvent event = new LoginEvent(IpPlayer.getPlayer(co.getUniqueId(), () -> null), co.getUniqueId(), co.getName(), e.isCancelled() ? Result.KICK_BANNED : Result.ALLOWED, co.getAddress().getAddress(), co.getVirtualHost().getAddress(), e.getCancelReason());
		EventManager.callEvent(event);
		if(!event.getLoginResult().equals(Result.ALLOWED)) {
			e.setCancelled(true);
			e.setCancelReason(new ComponentBuilder(event.getKickMessage()).create());
		}
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		IpPlayer np = IpPlayer.getPlayer(p.getUniqueId(), () -> new BungeePlayer(p));
		PlayerConnectEvent event = new PlayerConnectEvent(np.getPlayer(), np, "");
		EventManager.callEvent(event);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		IpPlayer np = IpPlayer.getPlayer(p.getUniqueId(), () -> new BungeePlayer(p));
		PlayerLeaveEvent event = new PlayerLeaveEvent(np.getPlayer(), np, "");
		EventManager.callEvent(event);
	}
}
