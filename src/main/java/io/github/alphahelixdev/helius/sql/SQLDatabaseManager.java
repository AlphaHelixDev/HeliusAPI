/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql;


import io.github.alphahelixdev.helius.sql.handler.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.query.SQLQueryConnector;
import io.github.alphahelixdev.helius.sql.query.SQLTableWrapper;

import java.util.HashMap;
import java.util.Map;

public final class SQLDatabaseManager {
	private static final Map<SQLConnector, SQLTableHandler> HANDLERS = new HashMap<>();
	private static final Map<SQLQueryConnector, SQLTableWrapper> QUERY_HANDLERS = new HashMap<>();
	
	public static void addHandle(SQLConnector connector, SQLTableHandler handler) {
		SQLDatabaseManager.getHandlers().putIfAbsent(connector, handler);
	}
	
	public static Map<SQLConnector, SQLTableHandler> getHandlers() {
		return HANDLERS;
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
	
	public static Map<SQLQueryConnector, SQLTableWrapper> getQueryHandlers() {
		return QUERY_HANDLERS;
	}
	
	public String toString() {
		return "SQLDatabaseManager{}";
	}
}

