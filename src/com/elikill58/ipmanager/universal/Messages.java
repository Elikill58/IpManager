package com.elikill58.ipmanager.universal;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ipmanager.api.commands.CommandSender;
import com.elikill58.ipmanager.api.utils.Utils;
import com.elikill58.ipmanager.spigot.SpigotIpManager;

public class Messages {
	
	private static String get(String msg) {
		return SpigotIpManager.getInstance().getConfig().getString(msg);
	}
	
	public static List<String> getStringList(String key, String... placeholders){
		List<String> last = SpigotIpManager.getInstance().getConfig().getStringList(key);
		if(last.size() == 0)
			return new ArrayList<>();
		List<String> l = new ArrayList<>();
		for(String s : last) {
			for (int index = 0; index <= placeholders.length - 1; index += 2)
				s = s.replaceAll(placeholders[index], placeholders[index + 1]);
			l.add(Utils.coloredMessage(s));
		}
		return l;
	}
	
	public static String getMessage(boolean b) {
		return getMessage(b ? "yes" : "no");
	}
	
	public static String getMessage(String key, String... placeholders) {
		String message = get(key);
		if(message == null)
			message = key;
		for (int index = 0; index <= placeholders.length - 1; index += 2)
			message = message.replaceAll(placeholders[index], placeholders[index + 1]);
		return Utils.coloredMessage(message);
	}
	
	public static void sendMessage(CommandSender p, String key, String... placeholders) {
		p.sendMessage(getMessage(key, placeholders));
	}
	
	public static void sendMessageList(CommandSender p, String key, String... placeholders) {
		for(String s : getStringList(key, placeholders))
			p.sendMessage(s);
	}
}