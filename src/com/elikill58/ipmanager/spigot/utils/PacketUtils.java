package com.elikill58.ipmanager.spigot.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketUtils {

	private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	public static Class<?> CRAFT_PLAYER_CLASS, CRAFT_ENTITY_CLASS;
	
	static {
		try {
			CRAFT_PLAYER_CLASS = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer");
			CRAFT_ENTITY_CLASS = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftEntity");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Map is to reduce Reflection action which take more ressources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS = new HashMap<>();
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return clazz
	 */
	public static Class<?> getNmsClass(String name){
		return ALL_CLASS.computeIfAbsent(name, (s) -> {
			try {
				return Class.forName("net.minecraft.server." + VERSION + "." + name);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}
	
	/**
	 * Create a new instance of a packet (without any parameters)
	 * 
	 * @param packetName the name of the packet which is in NMS
	 * @return the created packet
	 */
	public static Object createPacket(String packetName) {
		try {
			return getNmsClass(packetName).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the current player ping
	 * 
	 * @param p the player
	 * @return the player ping
	 */
	public static int getPing(Player p) {
		try {
			Object entityPlayer = getEntityPlayer(p);
			return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Create and send a packet (with only one parameter)
	 * 
	 * @param p the player which will receive the packet
	 * @param packetName the name of the packet that will be created and sent
	 * @param type the constructor type of parameter
	 * @param data the data associated with parameter type
	 */
	public static void sendPacket(Player p, String packetName, Class<?> type, Object data) {
		try {
			sendPacket(p, getNmsClass(packetName).getConstructor(type).newInstance(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the packet to the specified player
	 * 
	 * @param p which will receive the packet
	 * @param packet the packet to sent
	 */
	public static void sendPacket(Player p, Object packet) {
		try {
			Object playerConnection = getPlayerConnection(p);
			playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get NMS player connection of specified player
	 * 
	 * @param p Player of which we want to get the player connection
	 * @return the NMS player connection
	 */
	public static Object getPlayerConnection(Player p) {
		try {
			Object entityPlayer = getEntityPlayer(p);
			return entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getEntityPlayer(Player p) {
		try {
			Object craftPlayer = CRAFT_PLAYER_CLASS.cast(p);
			return craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getNMSEntity(Entity et) {
		try {
			Object craftEntity = CRAFT_ENTITY_CLASS.cast(et);
			return craftEntity.getClass().getMethod("getHandle").invoke(craftEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
