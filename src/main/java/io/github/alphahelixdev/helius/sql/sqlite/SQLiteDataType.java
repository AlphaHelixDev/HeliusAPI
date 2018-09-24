package io.github.alphahelixdev.helius.sql.sqlite;

import io.github.alphahelixdev.helius.sql.SQLDataType;

public enum SQLiteDataType implements SQLDataType {
	NULL,
	INTEGER,
	REAL,
	TEXT,
	BLOB;
	
	@Override
	public String sqlNameWithSize(int size) {
		if(size == -1)
			return sqlName();
		return sqlName() + "(" + size + ")";
	}
	
	@Override
	public String sqlName() {
		return this.name();
	}
}
