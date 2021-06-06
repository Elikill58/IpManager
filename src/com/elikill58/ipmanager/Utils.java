package com.elikill58.ipmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

public class Utils {

	private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	
	public static String getServerURL() {
		return new Random().nextBoolean() ? "https://api.eliapp.fr/" : "https://api.infinitymc.fr/";
	}
	
	public static List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		try {
			Class<?> mcServer = Class.forName("net.minecraft.server." + VERSION + ".MinecraftServer");
			Object server = mcServer.getMethod("getServer").invoke(mcServer);
			Object craftServer = server.getClass().getField("server").get(server);
			Object getted = craftServer.getClass().getMethod("getOnlinePlayers").invoke(craftServer);
			if (getted instanceof Player[])
				for (Player obj : (Player[]) getted)
					list.add(obj);
			else if (getted instanceof List)
				for (Object obj : (List<?>) getted)
					list.add((Player) obj);
			else
				System.out.println("Unknow getOnlinePlayers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static int getMultipleOf(int i, int multiple, int more) {
		while (i % multiple != 0)
			i += more;
		return i;
	}

	public static String applyColorCodes(String message) {
		return message.replaceAll("&r", ChatColor.RESET.toString()).replaceAll("&0", ChatColor.BLACK.toString())
				.replaceAll("&1", ChatColor.DARK_BLUE.toString()).replaceAll("&2", ChatColor.DARK_GREEN.toString())
				.replaceAll("&3", ChatColor.DARK_AQUA.toString()).replaceAll("&4", ChatColor.DARK_RED.toString())
				.replaceAll("&5", ChatColor.DARK_PURPLE.toString()).replaceAll("&6", ChatColor.GOLD.toString())
				.replaceAll("&7", ChatColor.GRAY.toString()).replaceAll("&8", ChatColor.DARK_GRAY.toString())
				.replaceAll("&9", ChatColor.BLUE.toString()).replaceAll("&a", ChatColor.GREEN.toString())
				.replaceAll("&b", ChatColor.AQUA.toString()).replaceAll("&c", ChatColor.RED.toString())
				.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString()).replaceAll("&e", ChatColor.YELLOW.toString())
				.replaceAll("&f", ChatColor.WHITE.toString()).replaceAll("&l", ChatColor.BOLD.toString())
				.replaceAll("&o", ChatColor.ITALIC.toString()).replaceAll("&m", ChatColor.STRIKETHROUGH.toString())
				.replaceAll("&n", ChatColor.UNDERLINE.toString()).replaceAll("&k", ChatColor.MAGIC.toString());
	}

	public static File copy(Plugin pl, String fileName, File f) {
		try (InputStream in = pl.getResource(fileName); OutputStream out = new FileOutputStream(f)) {
			ByteStreams.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getContentFromUrl(String stringUrl) {
		try {
			URL url = new URL(stringUrl);
			//doTrustToCertificates();
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			/*
			 * connection.setConnectTimeout(5); connection.setReadTimeout(5);
			 */
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US) Gecko/20100316 Firefox/3.6.2");
			connection.setUseCaches(true);
			connection.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String content = "";
			String input;
			while ((input = br.readLine()) != null)
				content = content + input;
			br.close();
			return content;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static void doTrustToCertificates() throws Exception {
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
					IpManager.getInstance().getLogger().warning("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
				}
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
}
