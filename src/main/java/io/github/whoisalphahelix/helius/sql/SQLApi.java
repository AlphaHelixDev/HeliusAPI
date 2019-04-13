/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package io.github.whoisalphahelix.helius.sql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.whoisalphahelix.helius.sql.exceptions.NoConnectionException;
import io.github.whoisalphahelix.helius.sql.handler.SQLTableHandler;
import io.github.whoisalphahelix.helius.sql.mysql.MySQLConnector;
import io.github.whoisalphahelix.helius.sql.mysql.MySQLQueryConnector;
import io.github.whoisalphahelix.helius.sql.query.SQLTableWrapper;
import io.github.whoisalphahelix.helius.sql.sqlite.SQLiteConnector;
import io.github.whoisalphahelix.helius.sql.sqlite.SQLiteQueryConnector;

import java.io.File;
import java.io.IOException;

public class SQLApi {
	private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
	
	public static SQLTableHandler connectSQLite(String database, Class<?> table) throws NoConnectionException {
		createDBFile(database);
		SQLiteConnector connector = new SQLiteConnector(() -> database);
		connector.connect(table);
		return connector.handler();
	}
	
	private static void createDBFile(String database) throws NoConnectionException {
		File dbFile = new File(database);
		if(dbFile.getParentFile().mkdirs()) {
			try {
				dbFile.createNewFile();
			} catch(IOException e) {
				throw new NoConnectionException(database);
			}
		}
	}
	
	public static SQLTableWrapper connectSQLite(String database, String table) throws NoConnectionException {
		createDBFile(database);
		SQLiteQueryConnector connector = new SQLiteQueryConnector(() -> database);
		connector.connect();
		return connector.handler(table);
	}
	
	public static SQLTableHandler connectMySQL(SQLInformation information, Class<?> table) throws NoConnectionException {
		MySQLConnector connector = new MySQLConnector(information);
		connector.connect(table);
		return connector.handler();
	}
	
	public static SQLTableWrapper connectMySQL(SQLInformation information, String table) throws NoConnectionException {
		MySQLQueryConnector connector = new MySQLQueryConnector(information);
		connector.connect();
		return connector.handler(table);
	}
	
	public static Gson gson() {
		return GSON_BUILDER.create();
	}
}

