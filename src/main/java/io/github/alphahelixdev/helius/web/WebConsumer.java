package io.github.alphahelixdev.helius.web;

import io.github.alphahelixdev.helius.Helius;

public interface WebConsumer<T> {
	
	void success(T t);
	
	default void fail(String message) {
		Helius.getLogger().warning(message);
	}
	
	default void fail(Exception e) {
		e.printStackTrace();
	}
}
