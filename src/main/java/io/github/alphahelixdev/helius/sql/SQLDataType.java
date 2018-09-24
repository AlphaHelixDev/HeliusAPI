package io.github.alphahelixdev.helius.sql;

public interface SQLDataType {
	
	default String sqlNameWithSize(int size) {
		return sqlName();
	}
	
	String sqlName();
}
