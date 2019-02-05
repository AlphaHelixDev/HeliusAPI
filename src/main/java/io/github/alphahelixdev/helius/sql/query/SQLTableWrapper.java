/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql.query;

import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SQLTableWrapper {
	private final SQLQueryConnector connector;
	private String table;
	private List<SQLColumn> columns;
	
	public SQLTableWrapper(SQLQueryConnector connector) {
		this.connector = connector;
	}
	
	public /* varargs */ SQLTableWrapper create(SQLColumn... sqlColumns) {
		new Thread(() -> {
			StringBuilder tableinfo = new StringBuilder();
			for(SQLColumn column : sqlColumns) {
				tableinfo.append(column);
			}
			String info = tableinfo.toString().replaceFirst("(?s)(.*),", "$1");
			this.setColumns(new ArrayList<>(Arrays.asList(sqlColumns)));
			if(this.doSyncChecks()) {
				try {
					String qry = "CREATE TABLE IF NOT EXISTS " + this.getTable() + "(" + info + ");";
					PreparedStatement prepstate = this.getConnector().connect().prepareStatement(qry);
					prepstate.execute();
				} catch(NoConnectionException | SQLException qry) {
					// empty catch block
				}
			}
		}).start();
		return this;
	}
	
	private boolean doSyncChecks() {
		if(this.getConnector() != null) {
			return this.getConnector().isConnected();
		}
		return false;
	}
	
	public String getTable() {
		return this.table;
	}
	
	public SQLQueryConnector getConnector() {
		return this.connector;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public SQLTableWrapper addForeignKey(SQLColumn column, String otherTable, String otherColumn) {
		return this.addForeignKey(column.getName(), otherTable, otherColumn);
	}
	
	public SQLTableWrapper addForeignKey(String changeColumn, String otherTable, String otherColumn) {
		return this.customQuery("ALTER TABLE " + this.getTable() + " ADD FOREIGN KEY (" + changeColumn + ") REFERENCES " + otherTable + "(" + otherColumn + ") ON UPDATE CASCADE", resultSet -> {});
	}
	
	public SQLTableWrapper customQuery(String qry, Consumer<ResultSet> callback) {
		this.doChecks(connection -> {
			try {
				callback.accept(connection.prepareStatement(qry).executeQuery());
			} catch(SQLException ignored) {
				this.syncedCallback(null, callback);
			}
		});
		return this;
	}
	
	private SQLTableWrapper doChecks(Consumer<Connection> runnable) {
		new Thread(() -> {
			if(this.doSyncChecks()) {
				try {
					runnable.accept(this.getConnector().connect());
				} catch(NoConnectionException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return this;
	}
	
	public <T> void syncedCallback(T obj, Consumer<T> consumer) {
		new Thread(() -> consumer.accept(obj)).start();
	}
	
	public SQLTableWrapper drop() {
		this.doChecks(connection -> {
			try {
				connection.prepareStatement("DROP TABLE " + this.getTable() + ";").executeQuery();
			} catch(SQLException sQLException) {
				// empty catch block
			}
		});
		return this;
	}
	
	public SQLTableWrapper empty() {
		this.doChecks(connection -> {
			try {
				connection.prepareStatement("DELETE FROM " + this.getTable() + ";").executeUpdate();
			} catch(SQLException sQLException) {
				// empty catch block
			}
		});
		return this;
	}
	
	public SQLTableWrapper remove(String condition, String value) {
		this.doChecks(connection -> {
			try {
				connection.prepareStatement("DELETE FROM " + this.getTable() + " WHERE(" + condition + "='" + value + "')").executeUpdate();
			} catch(SQLException sQLException) {
				// empty catch block
			}
		});
		return this;
	}
	
	public /* varargs */ SQLTableWrapper insert(String... values) {
		new Thread(() -> {
			StringBuilder builder = new StringBuilder();
			for(String str : values) {
				builder.append(",").append("'").append(str).append("'");
			}
			StringBuilder builder2 = new StringBuilder();
			for(SQLColumn c : this.columns) {
				builder2.append(",").append(c.getName());
			}
			String info = builder2.toString().replaceFirst(",", "");
			if(this.doSyncChecks()) {
				try {
					if(info.isEmpty()) {
						return;
					}
					this.getConnector().connect().prepareStatement("INSERT INTO " + this.getTable() + " (" + info + ") VALUES (" + builder.toString().replaceFirst(",", "") + ");").executeUpdate();
				} catch(NoConnectionException | SQLException exception) {
					// empty catch block
				}
			}
		}).start();
		return this;
	}
	
	public SQLTableWrapper update(String condition, String conditionValue, String column, String updatevalue) {
		this.doChecks(connection -> {
			try {
				String qry = "UPDATE " + this.getTable() + " SET " + column + "=? WHERE " + condition + "=?;";
				PreparedStatement prepstate = connection.prepareStatement(qry);
				prepstate.setString(1, updatevalue);
				prepstate.setString(2, conditionValue);
				prepstate.executeUpdate();
			} catch(SQLException qry) {
				// empty catch block
			}
		});
		return this;
	}
	
	public SQLTableWrapper dropColumn(String columnName) {
		ArrayList<SQLColumn> columnList = new ArrayList<SQLColumn>();
		for(SQLColumn column : this.getColumns()) {
			if(column.getName().equals(columnName)) continue;
			columnList.add(column);
		}
		this.setColumns(columnList);
		return this.customQuery("ALTER TABLE " + this.getTable() + " DROP COLUMN " + columnName + ";", result -> {});
	}
	
	public List<SQLColumn> getColumns() {
		return this.columns;
	}
	
	public void setColumns(List<SQLColumn> columns) {
		this.columns = columns;
	}
	
	public SQLTableWrapper addColumn(SQLColumn column) {
		this.getColumns().add(column);
		return this.customQuery("ALTER TABLE " + this.getTable() + " ADD " + column + ";", resultSet -> {});
	}
	
	public SQLTableWrapper contains(String condition, String value, Consumer<Boolean> check) {
		return this.getResult(condition, value, condition, result -> check.accept(result != null));
	}
	
	public <T> SQLTableWrapper getResult(String condition, String value, String column, Consumer<T> callback) {
		this.doChecks(connection -> this.syncedCallback(this.getResult(condition, value, column), callback));
		return this;
	}
	
	public boolean syncContains(String condition, String value) {
		return this.getResult(condition, value, condition) != null;
	}
	
	public <T> T getResult(String condition, String value, String column) {
		if(this.doSyncChecks()) {
			try {
				String qry = "SELECT * FROM " + this.getTable() + " WHERE " + condition + "=?;";
				PreparedStatement prepstate = this.getConnector().connect().prepareStatement(qry);
				prepstate.setString(1, value);
				ResultSet rs = prepstate.executeQuery();
				if(rs == null) {
					return null;
				}
				if(rs.next()) {
					return (T) rs.getObject(column);
				}
			} catch(NoConnectionException | SQLException ignored) {
				return null;
			}
		}
		return null;
	}
	
	public SQLTableWrapper getList(String column, Consumer<List<Object>> callback) {
		return this.getList(column, -1, callback);
	}
	
	public SQLTableWrapper getList(String column, int limit, Consumer<List<Object>> callback) {
		this.doChecks(connection -> this.syncedCallback(this.getSyncList(column, limit), callback));
		return this;
	}
	
	public List<Object> getSyncList(String column, int limit) {
		String qry = "SELECT " + column + " FROM " + this.getTable() + " LIMIT " + limit + ";";
		if(limit == -1) {
			qry = "SELECT " + column + " FROM " + this.getTable() + ";";
		}
		try {
			ResultSet rs = this.getConnector().connect().prepareStatement(qry).executeQuery();
			ArrayList<Object> objs = new ArrayList<Object>();
			while(rs.next()) {
				objs.add(rs.getObject(column));
			}
			return objs;
		} catch(NoConnectionException | SQLException ignored) {
			return new ArrayList<Object>();
		}
	}
	
	public List<Object> getSyncList(String column) {
		return this.getSyncList(column, -1);
	}
	
	public SQLTableWrapper getRows(Consumer<List<List<String>>> callback) {
		return this.getRows(-1, callback);
	}
	
	public SQLTableWrapper getRows(int limit, Consumer<List<List<String>>> callback) {
		this.doChecks(connection -> this.syncedCallback(this.getSyncRows(limit), callback));
		return this;
	}
	
	public List<List<String>> getSyncRows(int limit) {
		String qry = "SELECT * FROM " + this.getTable() + " LIMIT " + limit + ";";
		if(limit == -1) {
			qry = "SELECT * FROM " + this.getTable() + ";";
		}
		try {
			ResultSet rs = this.getConnector().connect().prepareStatement(qry).executeQuery();
			LinkedList<List<String>> res = new LinkedList<List<String>>();
			while(rs.next()) {
				ArrayList<String> rowObjects = new ArrayList<String>();
				for(String column : this.getColumnNames()) {
					rowObjects.add(rs.getString(column));
				}
				res.add(rowObjects);
			}
			return res;
		} catch(NoConnectionException | SQLException e) {
			return new ArrayList<List<String>>();
		}
	}
	
	public List<String> getColumnNames() {
		ArrayList<String> columnNames = new ArrayList<String>();
		for(int i = 0; i < this.getColumnAmount(); ++i) {
			if(this.getColumnName(i).isEmpty()) continue;
			columnNames.add(this.getColumnName(i));
		}
		return columnNames;
	}
	
	public int getColumnAmount() {
		return this.getColumns().size();
	}
	
	public String getColumnName(int column) {
		return this.getColumns().get(column).getName();
	}
	
	public SQLTableWrapper getCell(String column, int row, Consumer<String> callback) {
		this.doChecks(connection -> this.syncedCallback(this.getSyncCell(column, row), callback));
		return this;
	}
	
	public String getSyncCell(String column, int row) {
		for(int i = 0; i < this.getColumnAmount(); ++i) {
			if(!this.getColumnName(i).equals(column)) continue;
			return this.getSyncRows().get(row).get(i);
		}
		return "";
	}
	
	public List<List<String>> getSyncRows() {
		return this.getSyncRows(-1);
	}
	
	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		SQLQueryConnector $connector = this.getConnector();
		result = result * 59 + ($connector == null ? 43 : $connector.hashCode());
		String $table = this.getTable();
		result = result * 59 + ($table == null ? 43 : $table.hashCode());
		List<SQLColumn> $columns = this.getColumns();
		result = result * 59 + ($columns == null ? 43 : $columns.hashCode());
		return result;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof SQLTableWrapper)) {
			return false;
		}
		SQLTableWrapper other = (SQLTableWrapper) o;
		if(!other.canEqual(this)) {
			return false;
		}
		SQLQueryConnector this$connector = this.getConnector();
		SQLQueryConnector other$connector = other.getConnector();
		if(this$connector == null ? other$connector != null : !this$connector.equals(other$connector)) {
			return false;
		}
		String this$table = this.getTable();
		String other$table = other.getTable();
		if(this$table == null ? other$table != null : !this$table.equals(other$table)) {
			return false;
		}
		List<SQLColumn> this$columns = this.getColumns();
		List<SQLColumn> other$columns = other.getColumns();
		return this$columns == null ? other$columns == null : this$columns.equals(other$columns);
	}
	
	protected boolean canEqual(Object other) {
		return other instanceof SQLTableWrapper;
	}
	
	public String toString() {
		return "SQLTableWrapper(connector=" + this.getConnector() + ", table=" + this.getTable() + ", columns=" + this.getColumns() + ")";
	}
}

