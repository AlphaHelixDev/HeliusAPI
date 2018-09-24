package io.github.alphahelixdev.helius.sql;

import io.github.alphahelixdev.helius.sql.mysql.MySQLDataType;
import io.github.alphahelixdev.helius.sql.sqlite.SQLiteDataType;

public enum SQLType {
	SQLITE(SQLiteDataType.values()), MYSQL(MySQLDataType.values()), UNSUPPORTED(new MySQLDataType[0]);
	
	private SQLDataType[] dataTypes;
	
	SQLType(SQLDataType[] dataTypes) {
		this.dataTypes = dataTypes;
	}
}