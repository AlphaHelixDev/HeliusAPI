/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.mysql;


import io.github.whoisalphahelix.helius.sql.query.SQLDataType;

public enum MySQLDataType implements SQLDataType {
	INT,
	TINYINT,
	SMALLINT,
	MEDIUMINT,
	BIGINT,
	FLOAT,
	DOUBLE,
	DECIMAL,
	DATE,
	DATETIME,
	TIMESTAMP,
	TIME,
	YEAR,
	CHAR,
	VARCHAR,
	BLOB,
	TEXT,
	TINYBLOB,
	TINYTEXT,
	MEDIUMBLOB,
	MEDIUMTEXT,
	LONGBLOB,
	LONGTEXT,
	ENUM;
	
	
	MySQLDataType() {
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

