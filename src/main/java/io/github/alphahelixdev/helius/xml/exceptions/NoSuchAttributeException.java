package io.github.alphahelixdev.helius.xml.exceptions;

import io.github.alphahelixdev.helius.xml.XMLObject;

public class NoSuchAttributeException extends Exception {

	public NoSuchAttributeException(XMLObject object, String attribute) {
		super("Unable to find the attribute '" + attribute + "' inside '" + object.getName() + "'");
	}
}
