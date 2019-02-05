/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql;

public interface SQLInformation {
	String name();
	
	default String username() {
		return "root";
	}
	
	default String password() {
		return "password";
	}
	
	default String host() {
		return "localhost";
	}
	
	default int port() {
		return 3306;
	}
}

