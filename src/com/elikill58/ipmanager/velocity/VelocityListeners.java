package com.elikill58.ipmanager.velocity;

import java.util.UUID;

import com.elikill58.ipmanager.api.PlayerAddress;
import com.elikill58.ipmanager.api.Players;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.universal.account.IpPlayerAccountManager;
import com.elikill58.ipmanager.velocity.impl.entity.VelocityPlayer;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.text.TextComponent;

public class VelocityListeners {

	@Subscribe
	public void onLogin(com.velocitypowered.api.event.connection.LoginEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		LoginEvent event = new LoginEvent(IpPlayerAccountManager.getManager().getNow(uuid), uuid, p.getUsername(),
				e.getResult().isAllowed() ? Result.ALLOWED : Result.KICK_BANNED, new PlayerAddress(p.getRemoteAddress()),
				new PlayerAddress(p.getRemoteAddress()), "");
		EventManager.callEvent(event);
		if (!event.getLoginResult().equals(Result.ALLOWED))
			e.setResult(ResultedEvent.ComponentResult.denied(TextComponent.of(event.getKickMessage())));
	}

	@Subscribe
	public void onPostLogin(PostLoginEvent e) {
		Player p = e.getPlayer();
		PlayerConnectEvent event = new PlayerConnectEvent(
				Players.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p)),
				IpPlayerAccountManager.getManager().getNow(p.getUniqueId()), "");
		EventManager.callEvent(event);
	}

	@Subscribe
	public void onPlayerQuit(DisconnectEvent e) {
		Player p = e.getPlayer();
		PlayerLeaveEvent event = new PlayerLeaveEvent(Players.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p)),
				IpPlayerAccountManager.getManager().getNow(p.getUniqueId()), "");
		EventManager.callEvent(event);
	}
}
