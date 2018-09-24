package io.github.alphahelixdev.helius.sql.mysql;

import io.github.alphahelixdev.helius.sql.SQLDataType;

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
