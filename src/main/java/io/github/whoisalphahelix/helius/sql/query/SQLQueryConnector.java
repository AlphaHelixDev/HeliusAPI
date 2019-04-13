/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.query;

import io.github.whoisalphahelix.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;

public interface SQLQueryConnector {
	Connection connect() throws NoConnectionException;
	
	boolean disconnect();
	
	boolean isConnected();
	
	SQLTableWrapper handler();
	
	SQLTableWrapper handler(String var1);
}

