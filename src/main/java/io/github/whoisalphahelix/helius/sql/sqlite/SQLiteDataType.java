/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.sqlite;

import io.github.whoisalphahelix.helius.sql.query.SQLDataType;

public enum SQLiteDataType implements SQLDataType {
	NULL,
	INTEGER,
	REAL,
	TEXT,
	BLOB;
	
	
	SQLiteDataType() {
	}
	
	@Override
	public String sqlNameWithSize(int size) {
		if(size == -1) {
			return this.sqlName();
		}
		return this.sqlName() + "(" + size + ")";
	}
	
	@Override
	public String sqlName() {
		return this.name();
	}
}

