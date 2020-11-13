package com.elikill58.ipmanager.api.utils;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ipmanager.api.colors.ChatColor;

public class Utils {

	public static String coloredMessage(String msg) {
		return msg == null ? null : ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static List<String> coloredMessage(String... messages) {
		List<String> ret = new ArrayList<>();
		for (String message : messages) {
			ret.add(coloredMessage(message));
		}
		return ret;
	}
}
