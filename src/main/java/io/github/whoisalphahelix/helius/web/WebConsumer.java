package io.github.whoisalphahelix.helius.web;

import io.github.whoisalphahelix.helius.Helius;

public interface WebConsumer<T> {
	
	void success(T t);
	
	default void fail(String message) {
		Helius.getLogger().warning(message);
	}
	
	default void fail(Exception e) {
		e.printStackTrace();
	}
}
