package io.github.alphahelixdev.helius.xml;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class XMLPropertyObject extends XMLObject {
	public XMLPropertyObject(String name) {
		super(name);
	}
	
	public XMLPropertyObject(String name, XMLAttribute[] attributes) {
		super(name, attributes);
	}
	
	public XMLPropertyObject(XMLObject parent, String name) {
		super(parent, name);
	}
	
	public XMLPropertyObject(XMLObject parent, String name, XMLAttribute[] attributes) {
		super(parent, name, attributes);
	}
	
	@Override
	public List<XMLObject> getChilds() {
		return ImmutableList.of();
	}
	
	@Override
	public XMLObject addChild(XMLObject child) {
		return this;
	}
	
	public String asXML() {
		return "<" + this.getName() + this.attributeXML() + "/>";
	}
}
