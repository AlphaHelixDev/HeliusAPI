package io.github.alphahelixdev.helius.sql.sqlite;

import io.github.alphahelixdev.helius.sql.SQLConnector;
import io.github.alphahelixdev.helius.sql.SQLDatabaseManager;
import io.github.alphahelixdev.helius.sql.SQLInformation;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements SQLConnector {
	
	private final SQLInformation information;
	private Connection connection;
	
	public SQLiteConnector(SQLInformation information) {
		this.information = information;
	}
	
	@Override
	public Connection connect() throws NoConnectionException {
		if(isConnected()) {
			return connection;
		} else {
			try {
				disconnect();
				
				Class.forName("org.sqlite.JDBC");
				
				connection = DriverManager.getConnection(
						"jdbc:sqlite:" + information.name());
				
				SQLDatabaseManager.addHandle(this, new SQLTableHandler(this));
				
				return connection;
			} catch(SQLException | ClassNotFoundException ignore) {
				throw new NoConnectionException(information.name());
			}
		}
	}
	
	@Override
	public boolean disconnect() {
		if(isConnected()) {
			try {
				connection.close();
				SQLDatabaseManager.removeHandle(this);
				return true;
			} catch(SQLException ignored) {
			}
		}
		return false;
	}
	
	@Override
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch(SQLException e) {
			return false;
		}
	}
	
	@Override
	public SQLTableHandler handler() {
		if(SQLDatabaseManager.hasHandle(this))
			return SQLDatabaseManager.getHandle(this);
		return null;
	}
	
	@Override
	public SQLTableHandler handler(String table) {
		if(table.trim().isEmpty()) return handler();
		if(handler() != null) return handler().setTable(table);
		return null;
	}
}
