package io.github.alphahelixdev.helius.sql.sqlite;

import io.github.alphahelixdev.helius.sql.SQLConnector;
import io.github.alphahelixdev.helius.sql.SQLDatabaseManager;
import io.github.alphahelixdev.helius.sql.SQLInformation;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class SQLiteConnector implements SQLConnector {
	
	private final SQLInformation information;
	private Connection connection;
	
	public SQLiteConnector(SQLInformation information) {
		this.information = information;
	}
	
	@Override
	public Connection connect() throws NoConnectionException {
		if(this.isConnected()) {
			return this.getConnection();
		} else {
			try {
				this.disconnect();
				
				Class.forName("org.sqlite.JDBC");
				
				this.setConnection(DriverManager.getConnection(
						"jdbc:sqlite:" + this.getInformation().name()));
				
				SQLDatabaseManager.addHandle(this, new SQLTableHandler(this));
				
				return this.getConnection();
			} catch(SQLException | ClassNotFoundException ignore) {
				throw new NoConnectionException(this.getInformation().name());
			}
		}
	}
	
	@Override
	public boolean disconnect() {
		if(this.isConnected()) {
			try {
				this.getConnection().close();
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
			return this.getConnection() != null && !this.getConnection().isClosed();
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
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public SQLInformation getInformation() {
		return this.information;
	}
	
	public SQLiteConnector setConnection(Connection connection) {
		this.connection = connection;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getInformation(), this.getConnection());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SQLiteConnector that = (SQLiteConnector) o;
		return Objects.equals(this.getInformation(), that.getInformation()) &&
				Objects.equals(this.getConnection(), that.getConnection());
	}
	
	@Override
	public String toString() {
		return "SQLiteConnector{" +
				"                            information=" + this.information +
				",                             connection=" + this.connection +
				'}';
	}
}
