package io.github.whoisalphahelix.helius;

public interface Cache {
	
	int TIME = 30;
	
	void clear();
	
	default String clearMessage() {
		return "Cache cleared";
	}
	
	default void save() {
	}
}
