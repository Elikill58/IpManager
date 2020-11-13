package com.elikill58.ipmanager.api.events;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.elikill58.ipmanager.api.commands.CommandManager;
import com.elikill58.ipmanager.api.inventory.InventoryManager;
import com.elikill58.ipmanager.common.ConnectionEvents;
import com.elikill58.ipmanager.universal.Adapter;

public class EventManager {

	private static final HashMap<Class<?>, List<CallableEvent>> EVENT_METHOD = new HashMap<>();

	public static void load() {
		EVENT_METHOD.clear();
		
		registerEvent(new CommandManager());
		registerEvent(new InventoryManager());
		registerEvent(new ConnectionEvents());
	}
	
	/**
	 * Add all event in the given class
	 * Warn: all registered event are removed when doing /negativity reload.
	 * 
	 * @param src the given object which contains event method
	 */
	public static void registerEvent(Listeners src) {
		try {
			checkClass(src);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkClass(Listeners src) throws InstantiationException, IllegalAccessException {
		Class<?> clazz = src.getClass();
		for(Method m : clazz.getDeclaredMethods()) {
			if(!m.isAnnotationPresent(EventListener.class)) 
				continue;
			if(m.getParameterCount() > 1)
				Adapter.getAdapter().getLogger().error("Too many arguments for method " + m.getName() + " in " + clazz.getCanonicalName());
			else if(m.getParameterCount() == 0)
				Adapter.getAdapter().getLogger().error("You forget the event for the method " + m.getName() + " in " + clazz.getCanonicalName());
			else {
				Class<?> paramEvent = m.getParameterTypes()[0];
				if(isAssignableFrom(paramEvent)) {
					List<CallableEvent> list = EVENT_METHOD.getOrDefault(paramEvent, new ArrayList<>());
					list.add(new CallableEvent(m, src));
					EVENT_METHOD.put(paramEvent, list);
				} else {
					Adapter.getAdapter().getLogger().error(paramEvent.getCanonicalName() + " isn't an Event ! (Located at " + clazz.getCanonicalName() + ")");
				}
			}
		}
	}
	
	private static boolean isAssignableFrom(Class<?> clazz) {
		try {
			if(clazz.isAssignableFrom(Event.class))
				return true;
			for(Type type : clazz.getGenericInterfaces()) {
				if(type.getTypeName().equals(Event.class.getCanonicalName()))
					return true;
				else if(isAssignableFrom(Class.forName(type.getTypeName())))
					return true;
			}
			if(!clazz.getSuperclass().equals(Object.class))
				return isAssignableFrom(clazz.getSuperclass());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Call an event
	 * 
	 * @param ev the event which have to be called
	 */
	public static void callEvent(Event ev) {
		List<CallableEvent> methods = EVENT_METHOD.get(ev.getClass());
		if(methods != null)
			new ArrayList<>(methods).forEach((m) -> {
				try {
					m.call(ev);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}
	
	public static class CallableEvent {
		private final Method methodEvent;
		private final Object source;
		
		public CallableEvent(Method methodEvent, Object src) {
			this.methodEvent = methodEvent;
			this.source = src;
		}
		
		public void call(Event ev) {
			try {
				methodEvent.invoke(source, ev);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
