package com.elikill58.ipmanager.velocity;

import java.util.UUID;

import com.elikill58.ipmanager.api.IpPlayer;
import com.elikill58.ipmanager.api.events.EventManager;
import com.elikill58.ipmanager.api.events.player.LoginEvent;
import com.elikill58.ipmanager.api.events.player.PlayerConnectEvent;
import com.elikill58.ipmanager.api.events.player.PlayerLeaveEvent;
import com.elikill58.ipmanager.api.events.player.LoginEvent.Result;
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
		LoginEvent event = new LoginEvent(IpPlayer.getPlayer(uuid, () -> new VelocityPlayer(p)), uuid,
				p.getUsername(), e.getResult().isAllowed() ? Result.ALLOWED : Result.KICK_BANNED,
				p.getRemoteAddress().getAddress(), p.getRemoteAddress().getAddress(), "");
		EventManager.callEvent(event);
		if (!event.getLoginResult().equals(Result.ALLOWED))
			e.setResult(ResultedEvent.ComponentResult.denied(TextComponent.of(event.getKickMessage())));
	}

	@Subscribe
	public void onPostLogin(PostLoginEvent e) {
		Player p = e.getPlayer();
		IpPlayer np = IpPlayer.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p));
		PlayerConnectEvent event = new PlayerConnectEvent(np.getPlayer(), np, "");
		EventManager.callEvent(event);
	}

	@Subscribe
	public void onPlayerQuit(DisconnectEvent e) {
		Player p = e.getPlayer();
		IpPlayer np = IpPlayer.getPlayer(p.getUniqueId(), () -> new VelocityPlayer(p));
		PlayerLeaveEvent event = new PlayerLeaveEvent(np.getPlayer(), np, "");
		EventManager.callEvent(event);
	}
}
