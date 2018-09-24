package io.github.alphahelixdev.helius.sql.mysql;

import io.github.alphahelixdev.helius.sql.SQLConnector;
import io.github.alphahelixdev.helius.sql.SQLDatabaseManager;
import io.github.alphahelixdev.helius.sql.SQLInformation;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector implements SQLConnector {
	
	private final SQLInformation information;
	private Connection connection;
	
	public MySQLConnector(SQLInformation information) {
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
						"jdbc:mysql://" + information.host() + ":" + information.port() + "/" + information.name() + "?autoReconnect=true", information.username(), information.password());
				
				
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
