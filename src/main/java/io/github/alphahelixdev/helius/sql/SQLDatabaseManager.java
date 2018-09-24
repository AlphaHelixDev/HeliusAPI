package io.github.alphahelixdev.helius.sql;

import java.util.HashMap;
import java.util.Map;

public final class SQLDatabaseManager {
	
	private static final Map<SQLConnector, SQLTableHandler> handlers = new HashMap<>();
	
	public static void addHandle(SQLConnector connector, SQLTableHandler handler) {
		handlers.putIfAbsent(connector, handler);
	}
	
	public static void removeHandle(SQLConnector connector) {
		handlers.remove(connector);
	}
	
	public static boolean hasHandle(SQLConnector connector) {
		return handlers.containsKey(connector);
	}
	
	public static SQLTableHandler getHandle(SQLConnector connector) {
		return handlers.get(connector);
	}
}
