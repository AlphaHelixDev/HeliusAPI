/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.mysql;

import io.github.whoisalphahelix.helius.sql.SQLDatabaseManager;
import io.github.whoisalphahelix.helius.sql.SQLInformation;
import io.github.whoisalphahelix.helius.sql.exceptions.NoConnectionException;
import io.github.whoisalphahelix.helius.sql.query.SQLQueryConnector;
import io.github.whoisalphahelix.helius.sql.query.SQLTableWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLQueryConnector
		implements SQLQueryConnector {
	private final SQLInformation information;
	private Connection connection;
	
	public MySQLQueryConnector(SQLInformation information) {
		this.information = information;
	}
	
	@Override
	public Connection connect() throws NoConnectionException {
		if(this.isConnected()) {
			return this.getConnection();
		}
		try {
			this.disconnect();
			Class.forName("org.sqlite.JDBC");
			this.setConnection(DriverManager.getConnection("jdbc:mysql://" + this.getInformation().host() + ":" + this.getInformation().port() + "/" + this.getInformation().name() + "?autoReconnect=true", this.getInformation().username(), this.getInformation().password()));
			SQLDatabaseManager.getQueryHandlers().put(this, new SQLTableWrapper(this));
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
				SQLDatabaseManager.getQueryHandlers().remove(this);
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
	public SQLTableWrapper handler() {
		return SQLDatabaseManager.getQueryHandlers().getOrDefault(this, null);
	}
	
	@Override
	public SQLTableWrapper handler(String table) {
		if(table.trim().isEmpty()) {
			return this.handler();
		}
		if(this.handler() != null) {
			SQLTableWrapper re = this.handler();
			re.setTable(table);
			return re;
		}
		return null;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public SQLInformation getInformation() {
		return this.information;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		SQLInformation $information = this.getInformation();
		result = result * 59 + ($information == null ? 43 : $information.hashCode());
		Connection $connection = this.getConnection();
		result = result * 59 + ($connection == null ? 43 : $connection.hashCode());
		return result;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof MySQLQueryConnector)) {
			return false;
		}
		MySQLQueryConnector other = (MySQLQueryConnector) o;
		if(!other.canEqual(this)) {
			return false;
		}
		SQLInformation this$information = this.getInformation();
		SQLInformation other$information = other.getInformation();
		if(this$information == null ? other$information != null : !this$information.equals(other$information)) {
			return false;
		}
		Connection this$connection = this.getConnection();
		Connection other$connection = other.getConnection();
		return this$connection == null ? other$connection == null : this$connection.equals(other$connection);
	}
	
	protected boolean canEqual(Object other) {
		return other instanceof MySQLQueryConnector;
	}
	
	public String toString() {
		return "MySQLQueryConnector(information=" + this.getInformation() + ", connection=" + this.getConnection() + ")";
	}
}

