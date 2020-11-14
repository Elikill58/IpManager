package com.elikill58.ipmanager.api;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PlayerAddress {
	
	private final String host;
	private final int port;
	
	/**
	 * Create a new PlayerAddress.
	 * 
	 * @param soc the inet socket address
	 */
	public PlayerAddress(InetSocketAddress soc) {
		this.host = (soc.getAddress() != null ? soc.getAddress().getHostAddress() : soc.getHostName()).replaceAll("localhost", "127.0.0.1");
		this.port = soc.getPort();
	}
	
	/**
	 * Create a new PlayerAddress.
	 * Warn: this doesn't support player port.
	 * Prefer use {@link #PlayerAddress(InetSocketAddress)}
	 * 
	 * @param addr the inet address
	 */
	public PlayerAddress(InetAddress addr) {
		this.host = addr.getHostAddress();
		this.port = -1;
	}
	
	/**
	 * Get the host (format: IP)
	 * 
	 * @return the host IP
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Get the port (or -1 if unknown)
	 * 
	 * @return the host port
	 */
	public int getPort() {
		return port;
	}
}
