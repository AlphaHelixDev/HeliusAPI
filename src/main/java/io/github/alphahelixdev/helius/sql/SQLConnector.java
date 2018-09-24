package io.github.alphahelixdev.helius.sql;

import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;

public interface SQLConnector {
	
	Connection connect() throws NoConnectionException;
	
	boolean disconnect();
	
	boolean isConnected();
	
	SQLTableHandler handler();
	
	SQLTableHandler handler(String table);
	
}
