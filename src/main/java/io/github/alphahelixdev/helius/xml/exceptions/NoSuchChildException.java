package io.github.alphahelixdev.helius.xml.exceptions;

import io.github.alphahelixdev.helius.xml.XMLObject;

public class NoSuchChildException extends Exception {

	public NoSuchChildException(XMLObject object, String childName) {
		super("Unable to find '" + childName + "' inside '" + object.getName() + "'");
	}
}
