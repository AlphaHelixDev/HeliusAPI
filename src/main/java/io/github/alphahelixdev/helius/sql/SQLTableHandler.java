package io.github.alphahelixdev.helius.sql;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SQLTableHandler {
	
	private final SQLConnector connector;
	private String table;
	private List<SQLColumn> columns;
	
	
	public SQLTableHandler(SQLConnector connector) {
		this.connector = connector;
	}
	
	public String getTable() {
		return table;
	}
	
	public SQLTableHandler setTable(String table) {
		this.table = table;
		return this;
	}
	
	public SQLTableHandler create(SQLColumn... sqlColumns) {
		Helius.runAsync(() -> {
			StringBuilder tableinfo = new StringBuilder();
			
			for(SQLColumn column : sqlColumns) {
				tableinfo.append(column);
			}
			
			String info = Helius.replaceLast(tableinfo.toString(), ",", "");
			
			columns = new ArrayList<>(Arrays.asList(sqlColumns));
			
			if(doSyncChecks()) {
				try {
					String qry = "CREATE TABLE IF NOT EXISTS " + table + "(" + info + ");";
					PreparedStatement prepstate = connector.connect().prepareStatement(qry);
					prepstate.execute();
				} catch(SQLException | NoConnectionException ignored) {
				}
			}
			
			return true;
		});
		return this;
	}
	
	private boolean doSyncChecks() {
		if(connector != null)
			return connector.isConnected();
		return false;
	}
	
	public SQLTableHandler drop() {
		doChecks(() -> {
			try {
				connector.connect().prepareStatement("DROP TABLE " + table + ";").executeQuery();
			} catch(SQLException | NoConnectionException ignored) {
			}
		});
		return this;
	}
	
	private SQLTableHandler doChecks(Runnable runnable) {
		Helius.runAsync(() -> {
			if(doSyncChecks())
				runnable.run();
			return true;
		});
		return this;
	}
	
	public SQLTableHandler empty() {
		doChecks(() -> {
			try {
				connector.connect().prepareStatement("DELETE FROM " + table + ";").executeUpdate();
			} catch(SQLException | NoConnectionException ignored) {
			}
		});
		return this;
	}
	
	public SQLTableHandler remove(String condition, String value) {
		doChecks(() -> {
			try {
				connector.connect().prepareStatement("DELETE FROM " + table + " WHERE(" + condition + "='" + value + "')").executeUpdate();
			} catch(SQLException | NoConnectionException ignored) {
			}
		});
		return this;
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
			
			if(doSyncChecks()) {
				try {
					if(info.isEmpty()) return false;
					connector.connect().prepareStatement("INSERT INTO " + table + " (" + info + ") VALUES (" + builder.toString().replaceFirst(",", "") + ");").executeUpdate();
				} catch(SQLException | NoConnectionException ignored) {
				}
			}
			
			return true;
		});
		return this;
	}
	
	public SQLTableHandler update(String condition, String conditionValue, String column, String updatevalue) {
		doChecks(() -> {
			try {
				String qry = "UPDATE " + table + " SET " + column + "=? WHERE " + condition + "=?;";
				PreparedStatement prepstate = connector.connect().prepareStatement(qry);
				prepstate.setString(1, updatevalue);
				prepstate.setString(2, conditionValue);
				
				prepstate.executeUpdate();
			} catch(SQLException | NoConnectionException ignored) {
			}
		});
		return this;
	}
	
	public SQLTableHandler dropColumn(String columnName) {
		List<SQLColumn> columnList = new ArrayList<>();
		for(SQLColumn column : columns)
			if(!column.getName().equals(columnName)) columnList.add(column);
		
		columns = columnList;
		
		return customQuery("ALTER TABLE " + table + " DROP COLUMN " + columnName + ";", result -> {});
	}
	
	public SQLTableHandler customQuery(String qry, Consumer<ResultSet> callback) {
		doChecks(() -> {
			try {
				callback.accept(connector.connect().prepareStatement(qry).executeQuery());
			} catch(SQLException | NoConnectionException ignored) {
				syncedCallback(null, callback);
			}
		});
		return this;
	}
	
	public <T> void syncedCallback(T obj, Consumer<T> consumer) {
		new Thread(() -> consumer.accept(obj)).start();
	}
	
	public SQLTableHandler addColumn(SQLColumn column) {
		this.columns.add(column);
		return customQuery("ALTER TABLE " + table + " ADD " + column + ";", resultSet -> {});
	}
	
	public SQLTableHandler contains(String condition, String value, Consumer<Boolean> check) {
		return getResult(condition, value, condition, result -> check.accept(result != null));
	}
	
	public <T> SQLTableHandler getResult(String condition, String value, String column, Consumer<T> callback) {
		doChecks(() -> syncedCallback(getResult(condition, value, column), callback));
		return this;
	}
	
	public <T> T getResult(String condition, String value, String column) {
		if(connector != null) {
			if(connector.isConnected()) {
				try {
					String qry = "SELECT * FROM " + table + " WHERE " + condition + "=?;";
					PreparedStatement prepstate = connector.connect().prepareStatement(qry);
					
					prepstate.setString(1, value);
					ResultSet rs = prepstate.executeQuery();
					
					if(rs == null)
						return null;
					
					if(rs.next())
						return (T) rs.getObject(column);
					else
						return null;
				} catch(SQLException | NoConnectionException ignored) {
					return null;
				}
			}
		}
		
		return null;
	}
	
	public boolean syncContains(String condition, String value) {
		return getResult(condition, value, condition) != null;
	}
	
	public SQLTableHandler getList(String column, Consumer<List<Object>> callback) {
		return getList(column, -1, callback);
	}
	
	public SQLTableHandler getList(String column, int limit, Consumer<List<Object>> callback) {
		doChecks(() -> syncedCallback(getSyncList(column, limit), callback));
		return this;
	}
	
	public List<Object> getSyncList(String column, int limit) {
		String qry = "SELECT " + column + " FROM " + table + " LIMIT " + limit + ";";
		if(limit == -1)
			qry = "SELECT " + column + " FROM " + table + ";";
		
		try {
			ResultSet rs = connector.connect().prepareStatement(qry).executeQuery();
			
			List<Object> objs = new ArrayList<>();
			
			while(rs.next())
				objs.add(rs.getObject(column));
			
			return objs;
		} catch(SQLException | NoConnectionException ignored) {
			return new ArrayList<>();
		}
	}
	
	public List<Object> getSyncList(String column) {
		return getSyncList(column, -1);
	}
	
	public SQLTableHandler getRows(Consumer<List<List<String>>> callback) {
		return getRows(-1, callback);
	}
	
	public SQLTableHandler getRows(int limit, Consumer<List<List<String>>> callback) {
		doChecks(() -> syncedCallback(getSyncRows(limit), callback));
		return this;
	}
	
	public List<List<String>> getSyncRows(int limit) {
		String qry = "SELECT * FROM " + table + " LIMIT " + limit + ";";
		if(limit == -1)
			qry = "SELECT * FROM " + table + ";";
		
		try {
			ResultSet rs = connector.connect().prepareStatement(qry).executeQuery();
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
			if(!getColumnName(i).isEmpty())
				columnNames.add(getColumnName(i));
		}
		
		return columnNames;
	}
	
	public int getColumnAmount() {
		return columns.size();
	}
	
	/**
	 * Gets the column name at a specific place
	 *
	 * @param column the id of the column
	 *
	 * @return the column name
	 */
	public String getColumnName(int column) {
		return columns.get(column).getName();
	}
	
	public SQLTableHandler getCell(String column, int row, Consumer<String> callback) {
		doChecks(() -> syncedCallback(getSyncCell(column, row), callback));
		return this;
	}
	
	public String getSyncCell(String column, int row) {
		for(int i = 0; i < getColumnAmount(); i++) {
			if(getColumnName(i).equals(column)) {
				return getSyncRows().get(row).get(i);
			}
		}
		return "";
	}
	
	public List<List<String>> getSyncRows() {
		return getSyncRows(-1);
	}
}