package io.github.alphahelixdev.helius;

public interface Cache {
	
	int time = 30;
	
	void clear();
	
	default String clearMessage() {
		return "Cache cleared";
	}
}
