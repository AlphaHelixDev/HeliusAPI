/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql.query;

public interface SQLDataType {
	default String sqlNameWithSize(int size) {
		return this.sqlName();
	}
	
	String sqlName();
}

