package com.elikill58.ipmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class Messages {
	
	private static String get(String msg) {
		return IpManager.getInstance().getConfig().getString(msg);
	}
	
	public static List<String> getStringList(String key, String... placeholders){
		List<String> last = IpManager.getInstance().getConfig().getStringList(key);
		if(last.size() == 0)
			return new ArrayList<>();
		List<String> l = new ArrayList<>();
		for(String s : last) {
			for (int index = 0; index <= placeholders.length - 1; index += 2)
				s = s.replaceAll(placeholders[index], placeholders[index + 1]);
			l.add(Utils.applyColorCodes(s));
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
		return Utils.applyColorCodes(message);
	}
	
	public static void sendMessage(CommandSender p, String key, String... placeholders) {
		p.sendMessage(getMessage(key, placeholders));
	}
	
	public static void sendMessageList(CommandSender p, String key, String... placeholders) {
		for(String s : getStringList(key, placeholders))
			p.sendMessage(s);
	}
}
