/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql;

public abstract class SQLRunnable<T>
		implements Runnable {
	private volatile T returnee;
	
	public T getReturnee() {
		return this.returnee;
	}
	
	public SQLRunnable setReturnee(T returnee) {
		this.returnee = returnee;
		return this;
	}
}

