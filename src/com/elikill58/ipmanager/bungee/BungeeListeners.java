package com.elikill58.ipmanager.bungee;

import com.elikill58.ipmanager.api.PlayerAddress;
import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.entity.Player;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.bungee.impl.entity.BungeePlayer;
import com.elikill58.ipmanager.universal.account.IpPlayerAccount;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListeners implements Listener {

	@EventHandler
	public void onPreLogin(net.md_5.bungee.api.event.LoginEvent e) {
		PendingConnection co = e.getConnection();
		@SuppressWarnings("deprecation")
		LoginEvent event = new LoginEvent(IpPlayerAccountManager.getManager().getNow(co.getUniqueId()),
				co.getUniqueId(), co.getName(), e.isCancelled() ? Result.KICK_BANNED : Result.ALLOWED,
				new PlayerAddress(co.getVirtualHost()), new PlayerAddress(co.getAddress()), "");
		EventManager.callEvent(event);
		if (!event.getLoginResult().equals(Result.ALLOWED)) {
			e.setCancelled(true);
			e.setCancelReason(new ComponentBuilder(event.getKickMessage()).create());
		}
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		ProxiedPlayer pp = e.getPlayer();
		Player p = Players.getPlayer(pp.getUniqueId(), () -> new BungeePlayer(pp));
		IpPlayerAccount np = IpPlayerAccountManager.getManager().getNow(pp.getUniqueId());
		PlayerConnectEvent event = new PlayerConnectEvent(p, np, "");
		EventManager.callEvent(event);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer pp = e.getPlayer();
		Player p = Players.getPlayer(pp.getUniqueId(), () -> new BungeePlayer(pp));
		IpPlayerAccount np = IpPlayerAccountManager.getManager().getNow(pp.getUniqueId());
		PlayerLeaveEvent event = new PlayerLeaveEvent(p, np, "");
		EventManager.callEvent(event);
	}
}
