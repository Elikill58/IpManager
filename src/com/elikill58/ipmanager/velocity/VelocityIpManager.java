package com.elikill58.ipmanager.velocity;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;

import com.elikill58.ipmanager.universal.Adapter;
import com.elikill58.ipmanager.universal.Database;
import com.elikill58.ipmanager.universal.IpManager;
import com.elikill58.ipmanager.universal.dataStorage.IpManagerAccountStorage;
import com.elikill58.ipmanager.universal.utils.UniversalUtils;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "ipmanager", name = "IpManager", version = UniversalUtils.IP_MANAGER_VERSION,
        description = "Manage player IP - Remove security problem with proxy", authors = {"Elikill58"})
public class VelocityIpManager {
	
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityIpManager(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public ProxyServer getServer() {
    	return server;
    }

    public Logger getLogger() {
    	return logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    	getLogger().info("Loading IpManager");
	    
		Adapter.setAdapter(new VelocityAdapter(this));
		
		IpManager.init();
		
	    server.getEventManager().register(this, new VelocityListeners());
	    server.getCommandManager().register(new VelocityGetIpCommand(), "getip");

		IpManagerAccountStorage.setDefaultStorage("file");

    	getLogger().info("IpManager enabled");
	}

    @Subscribe
    public void onProxyDisable(ProxyShutdownEvent e) {
		Database.close();	
	}

    public final InputStream getResourceAsStream(final String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public final File getDataFolder() {
        return new File("./plugins/IpManager");
    }
}
