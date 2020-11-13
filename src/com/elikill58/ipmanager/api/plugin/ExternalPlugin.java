package com.elikill58.ipmanager.api.plugin;

import com.elikill58.ipmanager.api.IpManagerObject;

public abstract class ExternalPlugin extends IpManagerObject {
	
	/**
	 * Check if the plugin is enabled
	 * 
	 * @return true if the plugin is enabled
	 */
	public abstract boolean isEnabled();
}
