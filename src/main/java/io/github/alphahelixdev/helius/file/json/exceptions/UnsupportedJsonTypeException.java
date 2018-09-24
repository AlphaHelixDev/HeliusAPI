package io.github.alphahelixdev.helius.file.json.exceptions;

public class UnsupportedJsonTypeException extends Exception {
	
	public UnsupportedJsonTypeException(String message) {
		super("Unsupported json type " + message);
	}
}
