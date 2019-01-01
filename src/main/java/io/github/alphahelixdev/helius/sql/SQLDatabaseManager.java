package io.github.alphahelixdev.helius.sql;

import java.util.HashMap;
import java.util.Map;

public final class SQLDatabaseManager {
	
	private static final Map<SQLConnector, SQLTableHandler> HANDLERS = new HashMap<>();
	
	public static void addHandle(SQLConnector connector, SQLTableHandler handler) {
		SQLDatabaseManager.getHandlers().putIfAbsent(connector, handler);
	}
	
	public static Map<SQLConnector, SQLTableHandler> getHandlers() {
		return SQLDatabaseManager.HANDLERS;
	}
	
	public static void removeHandle(SQLConnector connector) {
		SQLDatabaseManager.getHandlers().remove(connector);
	}
	
	public static boolean hasHandle(SQLConnector connector) {
		return SQLDatabaseManager.getHandlers().containsKey(connector);
	}
	
	public static SQLTableHandler getHandle(SQLConnector connector) {
		return SQLDatabaseManager.getHandlers().get(connector);
	}
	
	@Override
	public String toString() {
		return "SQLDatabaseManager{}";
	}
}
