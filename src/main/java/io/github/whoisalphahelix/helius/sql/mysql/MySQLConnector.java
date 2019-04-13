/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.mysql;

import io.github.whoisalphahelix.helius.sql.SQLConnector;
import io.github.whoisalphahelix.helius.sql.SQLDatabaseManager;
import io.github.whoisalphahelix.helius.sql.SQLInformation;
import io.github.whoisalphahelix.helius.sql.exceptions.NoConnectionException;
import io.github.whoisalphahelix.helius.sql.handler.SQLTableHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector
		implements SQLConnector {
	private final SQLInformation information;
	private Connection connection;
	
	public MySQLConnector(SQLInformation information) {
		this.information = information;
	}
	
	@Override
	public Connection connect(Class<?> table) throws NoConnectionException {
		if(this.isConnected()) {
			return this.getConnection();
		}
		try {
			this.disconnect();
			Class.forName("org.sqlite.JDBC");
			this.setConnection(DriverManager.getConnection("jdbc:mysql://" + this.getInformation().host() + ":" + this.getInformation().port() + "/" + this.getInformation().name() + "?autoReconnect=true", this.getInformation().username(), this.getInformation().password()));
			SQLDatabaseManager.addHandle(this, new SQLTableHandler(this, table));
			return this.getConnection();
		} catch(ClassNotFoundException | SQLException ignore) {
			throw new NoConnectionException(this.getInformation().name());
		}
	}
	
	@Override
	public boolean disconnect() {
		if(this.isConnected()) {
			try {
				this.getConnection().close();
				SQLDatabaseManager.removeHandle(this);
				return true;
			} catch(SQLException sQLException) {
				// empty catch block
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
		if(SQLDatabaseManager.hasHandle(this)) {
			return SQLDatabaseManager.getHandle(this);
		}
		return null;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public SQLInformation getInformation() {
		return this.information;
	}
	
	public MySQLConnector setConnection(Connection connection) {
		this.connection = connection;
		return this;
	}
}

