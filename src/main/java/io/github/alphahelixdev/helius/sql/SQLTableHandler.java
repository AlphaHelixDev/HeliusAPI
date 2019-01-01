package io.github.alphahelixdev.helius.sql;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class SQLTableHandler {
	
	private final SQLConnector connector;
	private String table;
	private List<SQLColumn> columns;
	
	public SQLTableHandler(SQLConnector connector) {
		this.connector = connector;
	}
	
	public SQLTableHandler create(SQLColumn... sqlColumns) {
		Helius.runAsync(() -> {
			StringBuilder tableinfo = new StringBuilder();
			
			for(SQLColumn column : sqlColumns) {
				tableinfo.append(column);
			}
			
			String info = Helius.replaceLast(tableinfo.toString(), ",", "");
			
			this.setColumns(new ArrayList<>(Arrays.asList(sqlColumns)));
			
			if(doSyncChecks()) {
				try {
					String qry = "CREATE TABLE IF NOT EXISTS " + this.getTable() + "(" + info + ");";
					PreparedStatement prepstate = this.getConnector().connect().prepareStatement(qry);
					prepstate.execute();
				} catch(SQLException | NoConnectionException ignored) {
				}
			}
			
			return true;
		});
		return this;
	}
	
	private boolean doSyncChecks() {
		if(this.getConnector() != null)
			return this.getConnector().isConnected();
		return false;
	}
	
	public String getTable() {
		return this.table;
	}
	
	public SQLConnector getConnector() {
		return this.connector;
	}
	
	public SQLTableHandler setTable(String table) {
		this.table = table;
		return this;
	}
	
	public SQLTableHandler addForeignKey(SQLColumn column, String otherTable, String otherColumn) {
		return this.addForeignKey(column.getName(), otherTable, otherColumn);
	}
	
	public SQLTableHandler addForeignKey(String changeColumn, String otherTable, String otherColumn) {
		return this.customQuery("ALTER TABLE " + this.getTable() + " ADD FOREIGN KEY (" + changeColumn + ") REFERENCES " + otherTable + "(" + otherColumn + ")", resultSet -> {});
	}
	
	public SQLTableHandler customQuery(String qry, Consumer<ResultSet> callback) {
		doChecks((connection) -> {
			try {
				callback.accept(connection.prepareStatement(qry).executeQuery());
			} catch(SQLException ignored) {
				this.syncedCallback(null, callback);
			}
		});
		return this;
	}
	
	private SQLTableHandler doChecks(Consumer<Connection> runnable) {
		Helius.runAsync(() -> {
			if(doSyncChecks())
				runnable.accept(this.getConnector().connect());
			return true;
		});
		return this;
	}
	
	public SQLTableHandler drop() {
		doChecks((connection) -> {
			try {
				connection.prepareStatement("DROP TABLE " + this.getTable() + ";").executeQuery();
			} catch(SQLException ignored) {
			}
		});
		return this;
	}
	
	public SQLTableHandler empty() {
		doChecks((connection) -> {
			try {
				connection.prepareStatement("DELETE FROM " + this.getTable() + ";").executeUpdate();
			} catch(SQLException ignored) {
			}
		});
		return this;
	}
	
	public SQLTableHandler remove(String condition, String value) {
		doChecks((connection) -> {
			try {
				connection.prepareStatement("DELETE FROM " + this.getTable() + " WHERE(" + condition + "='" + value + "')").executeUpdate();
			} catch(SQLException ignored) {
			}
		});
		return this;
	}
	
	public <T> void syncedCallback(T obj, Consumer<T> consumer) {
		new Thread(() -> consumer.accept(obj)).start();
	}
	
	public SQLTableHandler insert(String... values) {
		Helius.runAsync(() -> {
			StringBuilder builder = new StringBuilder();
			
			for(String str : values)
				builder.append(",").append("\'").append(str).append("\'");
			
			StringBuilder builder2 = new StringBuilder();
			String info;
			
			for(SQLColumn c : columns) {
				builder2.append(",").append(c.getName());
			}
			
			info = builder2.toString().replaceFirst(",", "");
			
			if(this.doSyncChecks()) {
				try {
					if(info.isEmpty()) return false;
					this.getConnector().connect().prepareStatement("INSERT INTO " + this.getTable() + " (" + info + ") VALUES (" + builder.toString().replaceFirst(",", "") + ");").executeUpdate();
				} catch(SQLException | NoConnectionException ignored) {
				}
			}
			
			return true;
		});
		return this;
	}
	
	public SQLTableHandler update(String condition, String conditionValue, String column, String updatevalue) {
		doChecks((connection) -> {
			try {
				String qry = "UPDATE " + this.getTable() + " SET " + column + "=? WHERE " + condition + "=?;";
				PreparedStatement prepstate = connection.prepareStatement(qry);
				prepstate.setString(1, updatevalue);
				prepstate.setString(2, conditionValue);
				
				prepstate.executeUpdate();
			} catch(SQLException ignored) {
			}
		});
		return this;
	}
	
	public SQLTableHandler dropColumn(String columnName) {
		List<SQLColumn> columnList = new ArrayList<>();
		for(SQLColumn column : this.getColumns())
			if(!column.getName().equals(columnName)) columnList.add(column);
		
		this.setColumns(columnList);
		
		return this.customQuery("ALTER TABLE " + this.getTable() + " DROP COLUMN " + columnName + ";", result -> {});
	}
	
	public List<SQLColumn> getColumns() {
		return this.columns;
	}
	
	public SQLTableHandler setColumns(List<SQLColumn> columns) {
		this.columns = columns;
		return this;
	}
	
	public SQLTableHandler addColumn(SQLColumn column) {
		this.getColumns().add(column);
		return this.customQuery("ALTER TABLE " + this.getTable() + " ADD " + column + ";", resultSet -> {});
	}
	
	public SQLTableHandler contains(String condition, String value, Consumer<Boolean> check) {
		return this.getResult(condition, value, condition, result -> check.accept(result != null));
	}
	
	public <T> SQLTableHandler getResult(String condition, String value, String column, Consumer<T> callback) {
		doChecks((connection) -> syncedCallback(this.getResult(condition, value, column), callback));
		return this;
	}
	
	public boolean syncContains(String condition, String value) {
		return this.getResult(condition, value, condition) != null;
	}
	
	public <T> T getResult(String condition, String value, String column) {
		if(doSyncChecks()) {
			try {
				String qry = "SELECT * FROM " + this.getTable() + " WHERE " + condition + "=?;";
				PreparedStatement prepstate = this.getConnector().connect().prepareStatement(qry);
				
				prepstate.setString(1, value);
				ResultSet rs = prepstate.executeQuery();
				
				if(rs == null)
					return null;
				
				if(rs.next())
					return (T) rs.getObject(column);
			} catch(SQLException | NoConnectionException ignored) {
				return null;
			}
		}
		
		return null;
	}
	
	public SQLTableHandler getList(String column, Consumer<List<Object>> callback) {
		return this.getList(column, -1, callback);
	}
	
	public SQLTableHandler getList(String column, int limit, Consumer<List<Object>> callback) {
		doChecks((connection) -> this.syncedCallback(this.getSyncList(column, limit), callback));
		return this;
	}
	
	public List<Object> getSyncList(String column, int limit) {
		String qry = "SELECT " + column + " FROM " + this.getTable() + " LIMIT " + limit + ";";
		if(limit == -1)
			qry = "SELECT " + column + " FROM " + this.getTable() + ";";
		
		try {
			ResultSet rs = this.getConnector().connect().prepareStatement(qry).executeQuery();
			
			List<Object> objs = new ArrayList<>();
			
			while(rs.next())
				objs.add(rs.getObject(column));
			
			return objs;
		} catch(SQLException | NoConnectionException ignored) {
			return new ArrayList<>();
		}
	}
	
	public List<Object> getSyncList(String column) {
		return this.getSyncList(column, -1);
	}
	
	public SQLTableHandler getRows(Consumer<List<List<String>>> callback) {
		return this.getRows(-1, callback);
	}
	
	public SQLTableHandler getRows(int limit, Consumer<List<List<String>>> callback) {
		doChecks((connection) -> this.syncedCallback(this.getSyncRows(limit), callback));
		return this;
	}
	
	public List<List<String>> getSyncRows(int limit) {
		String qry = "SELECT * FROM " + this.getTable() + " LIMIT " + limit + ";";
		if(limit == -1)
			qry = "SELECT * FROM " + this.getTable() + ";";
		
		try {
			ResultSet rs = this.getConnector().connect().prepareStatement(qry).executeQuery();
			List<List<String>> res = new LinkedList<>();
			
			while(rs.next()) {
				List<String> rowObjects = new ArrayList<>();
				
				for(String column : getColumnNames()) {
					rowObjects.add(rs.getString(column));
				}
				
				res.add(rowObjects);
			}
			
			return res;
		} catch(SQLException | NoConnectionException e) {
			return new ArrayList<>();
		}
	}
	
	public List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<>();
		
		for(int i = 0; i < getColumnAmount(); i++) {
			if(!this.getColumnName(i).isEmpty())
				columnNames.add(getColumnName(i));
		}
		
		return columnNames;
	}
	
	public int getColumnAmount() {
		return this.getColumns().size();
	}
	
	/**
	 * Gets the column name at a specific place
	 *
	 * @param column the id of the column
	 *
	 * @return the column name
	 */
	public String getColumnName(int column) {
		return this.getColumns().get(column).getName();
	}
	
	public SQLTableHandler getCell(String column, int row, Consumer<String> callback) {
		doChecks((connection) -> this.syncedCallback(this.getSyncCell(column, row), callback));
		return this;
	}
	
	public String getSyncCell(String column, int row) {
		for(int i = 0; i < getColumnAmount(); i++) {
			if(this.getColumnName(i).equals(column)) {
				return this.getSyncRows().get(row).get(i);
			}
		}
		return "";
	}
	
	public List<List<String>> getSyncRows() {
		return this.getSyncRows(-1);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getConnector(), this.getTable(), this.getColumns());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SQLTableHandler that = (SQLTableHandler) o;
		return Objects.equals(this.getConnector(), that.getConnector()) &&
				Objects.equals(this.getTable(), that.getTable()) &&
				Objects.equals(this.getColumns(), that.getColumns());
	}
	
	@Override
	public String toString() {
		return "SQLTableHandler{" +
				"                            connector=" + this.connector +
				",                             table='" + this.table + '\'' +
				",                             columns=" + this.columns +
				'}';
	}
}