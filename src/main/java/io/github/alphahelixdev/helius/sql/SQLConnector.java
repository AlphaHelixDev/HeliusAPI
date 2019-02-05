/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql;

import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;
import io.github.alphahelixdev.helius.sql.handler.SQLTableHandler;

import java.sql.Connection;

public interface SQLConnector {
	Connection connect(Class<?> var1) throws NoConnectionException;
	
	boolean disconnect();
	
	boolean isConnected();
	
	SQLTableHandler handler();
}

