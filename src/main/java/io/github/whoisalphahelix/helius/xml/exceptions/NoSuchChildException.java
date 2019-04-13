package io.github.whoisalphahelix.helius.xml.exceptions;

import io.github.whoisalphahelix.helius.xml.XMLObject;

public class NoSuchChildException extends Exception {

	public NoSuchChildException(XMLObject object, String childName) {
		super("Unable to find '" + childName + "' inside '" + object.getName() + "'");
	}
}
