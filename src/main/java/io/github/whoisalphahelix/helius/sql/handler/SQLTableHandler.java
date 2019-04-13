/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.handler;

import io.github.whoisalphahelix.helius.sql.JsonHelper;
import io.github.whoisalphahelix.helius.sql.SQLConnector;
import io.github.whoisalphahelix.helius.sql.SQLRunnable;
import io.github.whoisalphahelix.helius.sql.annotations.Column;
import io.github.whoisalphahelix.helius.sql.annotations.Table;
import io.github.whoisalphahelix.helius.sql.exceptions.NoConnectionException;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SQLTableHandler {
	private final SQLConnector connector;
	private final Class<?> tableClass;
	private final String tableName;
	private final List<Field> classFields = new ArrayList<>();
	private final List<Column> columns = new ArrayList<>();
	
	public SQLTableHandler(SQLConnector connector, Class<?> table) {
		this.connector = connector;
		this.tableClass = table;
		this.tableName = table.getAnnotation(Table.class).name();
	}
	
	public SQLTableHandler createTable() {
		new Thread(() -> {
			StringBuilder tableInfo = new StringBuilder();
			this.findFields().stream().map(field -> field.getAnnotation(Column.class)).forEach(column -> {
				this.columns.add(column);
				tableInfo.append(column.name()).append(" ").append("BLOB").append(column.additionalQuery()).append(",");
			});
			String info = tableInfo.toString().replaceFirst("(?s)(.*),", "$1");
			if(this.doChecks()) {
				String qry = "CREATE TABLE IF NOT EXISTS " + this.getTableName() + "(" + info + ");";
				try {
					PreparedStatement prepstate = this.getConnector().connect(this.getTableClass()).prepareStatement(qry);
					prepstate.execute();
				} catch(NoConnectionException | SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return this;
	}
	
	private List<Field> findFields() {
		ArrayList<Field> fields = new ArrayList<Field>();
		Class<?> sup = this.tableClass;
		while(sup.getSuperclass() != null) {
			fields.addAll(Arrays.stream(sup.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Column.class)).peek(field -> field.setAccessible(true)).collect(Collectors.toList()));
			sup = sup.getSuperclass();
		}
		this.classFields.addAll(fields);
		return fields;
	}
	
	private boolean doChecks() {
		if(this.getConnector() != null) {
			return this.getConnector().isConnected();
		}
		return false;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public SQLConnector getConnector() {
		return this.connector;
	}
	
	public Class<?> getTableClass() {
		return this.tableClass;
	}
	
	public SQLTableHandler empty() {
		if(this.doChecks()) {
			try {
				this.getConnector().connect(this.getTableClass()).prepareStatement("DELETE FROM " + this.getTableName() + ";").executeUpdate();
			} catch(NoConnectionException | SQLException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public SQLTableHandler insertElements(Object o) {
		this.insert((String[]) this.getClassFields().stream().map(field -> {
			try {
				return JsonHelper.toJsonTree(field.get(o)).toString();
			} catch(IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}).filter(Objects::nonNull).toArray(String[]::new));
		return this;
	}
	
	public /* varargs */ SQLTableHandler insert(String... values) {
		new Thread(() -> {
			StringBuilder builder = new StringBuilder();
			for(String str : values) {
				builder.append(",").append("'").append(str).append("'");
			}
			StringBuilder builder2 = new StringBuilder();
			for(Column c : this.columns) {
				builder2.append(",").append(c.name());
			}
			String info = builder2.toString().replaceFirst(",", "");
			if(this.doChecks()) {
				try {
					if(info.isEmpty()) {
						return;
					}
					this.getConnector().connect(this.getTableClass()).prepareStatement("INSERT INTO " + this.getTableName() + " (" + info + ") VALUES (" + builder.toString().replaceFirst(",", "") + ");").executeUpdate();
				} catch(NoConnectionException | SQLException exception) {
					// empty catch block
				}
			}
		}).start();
		return this;
	}
	
	public List<Field> getClassFields() {
		return this.classFields;
	}
	
	public boolean contains(Object element) {
		return this.getElements().contains(element);
	}
	
	public List<Object> getElements() {
		SQLRunnable<List<Object>> fetcher = new SQLRunnable<List<Object>>() {
			
			@Override
			public void run() {
				ArrayList elements = new ArrayList();
				SQLTableHandler.this.getSyncRows().forEach(row -> {
					try {
						Object rowElement = SQLTableHandler.this.getTableClass().newInstance();
						List<Field> columnFields = SQLTableHandler.this.getClassFields();
						for(int i = 0; i < row.size(); ++i) {
							columnFields.get(i).set(rowElement, JsonHelper.fromJsonTree(row.get(i)));
						}
						elements.add(rowElement);
					} catch(ReflectiveOperationException e) {
						e.printStackTrace();
					}
				});
				this.setReturnee(elements);
			}
		};
		Thread fetchingThread = new Thread(fetcher);
		fetchingThread.start();
		try {
			fetchingThread.join(500L);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		return fetcher.getReturnee();
	}
	
	public List<List<String>> getSyncRows() {
		String qry = "SELECT * FROM " + this.getTableName() + ";";
		try {
			ResultSet rs = this.getConnector().connect(this.getTableClass()).prepareStatement(qry).executeQuery();
			LinkedList<List<String>> res = new LinkedList<List<String>>();
			while(rs.next()) {
				ArrayList<String> rowObjects = new ArrayList<String>();
				for(Column column : this.getColumns()) {
					rowObjects.add(rs.getString(column.name()));
				}
				res.add(rowObjects);
			}
			return res;
		} catch(NoConnectionException | SQLException e) {
			return new ArrayList<List<String>>();
		}
	}
	
	public List<Column> getColumns() {
		return this.columns;
	}
	
	public boolean hasKey(final String column, final String key) {
		SQLRunnable<Object> fetcher = new SQLRunnable<Object>() {
			
			@Override
			public void run() {
				this.setReturnee(SQLTableHandler.this.getResult(column, key, column));
			}
		};
		Thread fetchingThread = new Thread(fetcher);
		fetchingThread.start();
		try {
			fetchingThread.join(500L);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		return fetcher.getReturnee() != null;
	}
	
	public <T> T getResult(String condition, String value, String column) {
		if(this.doChecks()) {
			try {
				String qry = "SELECT * FROM " + this.getTableName() + " WHERE " + condition + "=?;";
				PreparedStatement prepstate = this.getConnector().connect(this.getTableClass()).prepareStatement(qry);
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
	
}

