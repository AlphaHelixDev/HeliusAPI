/*
 * Decompiled with CFR 0_132.
 */
package io.github.whoisalphahelix.helius.sql.exceptions;

public class NoConnectionException
		extends Exception {
	public NoConnectionException() {
		super("Unable to connect to database");
	}
	
	public NoConnectionException(String message) {
		super("Unable to connect to " + message);
	}
}

