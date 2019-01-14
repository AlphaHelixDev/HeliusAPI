package io.github.alphahelixdev.helius.xml;

import io.github.alphahelixdev.helius.xml.exceptions.NoSuchAttributeException;
import io.github.alphahelixdev.helius.xml.exceptions.NoSuchChildException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class XMLObject {
	
	private final XMLObject parent;
	private final String name;
	
	private List<XMLAttribute<?>> attributes;
	private List<XMLObject> childs;
	
	public XMLObject(String name) {
		this(null, name, new XMLAttribute<?>[]{}, new XMLObject[]{});
	}
	
	public XMLObject(XMLObject parent, String name, XMLAttribute<?>[] attributes, XMLObject[] childs) {
		this.parent = parent;
		this.name = name.replace(" ", "");
		this.attributes = new ArrayList<>(Arrays.asList(attributes));
		this.childs = new ArrayList<>(Arrays.asList(childs));
	}
	
	public XMLObject(String name, XMLAttribute<?>[] attributes) {
		this(null, name, attributes,
				new XMLObject[]{});
	}
	
	public XMLObject(String name, XMLObject[] childs) {
		this(null, name, new XMLAttribute[]{}, childs);
	}
	
	public XMLObject(String name, XMLAttribute<?>[] attributes, XMLObject[] childs) {
		this(null, name, attributes, childs);
	}
	
	public XMLObject(XMLObject parent, String name) {
		this(parent, name, new XMLAttribute<?>[]{}, new XMLObject[]{});
	}
	
	public XMLObject(XMLObject parent, String name, XMLAttribute<?>[] attributes) {
		this(parent, name, attributes, new XMLObject[]{});
	}
	
	public XMLObject(XMLObject parent, String name, XMLObject[] childs) {
		this(parent, name, new XMLAttribute<?>[]{}, childs);
	}
	
	public List<XMLAttribute<?>> getDeepAttributes() {
		List<XMLAttribute<?>> attributes = getAttributes();
		
		for(XMLObject child : this.getChilds())
			attributes.addAll(child.getDeepAttributes());
		
		return attributes;
	}
	
	public <V> XMLAttribute<V> findAttribute(String name, Class<V> valueClass) throws NoSuchAttributeException {
		return this.findAttribute(name, valueClass, false);
	}
	
	public <V> XMLAttribute<V> findAttribute(String name, Class<V> valueClass, boolean deep)
			throws NoSuchAttributeException {
		if(deep) {
			for(XMLAttribute<?> attribute : this.getDeepAttributes()) {
				if(attribute.name().equals(name) && attribute.value().getClass().equals(valueClass))
					return (XMLAttribute<V>) attribute;
			}
			throw new NoSuchAttributeException(this, name);
		}
		
		for(XMLAttribute<?> attribute : this.getAttributes()) {
			if(attribute.name().equals(name) && attribute.value().getClass().equals(valueClass))
				return (XMLAttribute<V>) attribute;
		}
		throw new NoSuchAttributeException(this, name);
	}
	
	public XMLObject findChild(String name) throws NoSuchChildException {
		for(XMLObject child : this.getChilds()) {
			if(child.getName().equals(name))
				return child;
		}
		throw new NoSuchChildException(this, name);
	}
	
	public XMLObject addChild(XMLObject child) {
		this.getChilds().add(child);
		return this;
	}
	
	public XMLObject addAttribute(XMLAttribute<?> attribute) {
		this.getAttributes().add(attribute);
		return this;
	}
	
	public String asXML() {
		return "<" + this.getName() + this.attributeXML() + ">" + this.childXML() + "</" + this.getName() + ">";
	}
	
	public String attributeXML() {
		StringBuilder attributes = new StringBuilder();
		
		if(!this.getAttributes().isEmpty()) {
			attributes.append(" ");
			for(XMLAttribute<?> attribute : this.getAttributes()) {
				attributes.append(attribute.name()).append("=\"").append(attribute.value()).append("\"").append(" ");
			}
			
			attributes.deleteCharAt(attributes.length() - 1);
		}
		
		return attributes.toString();
	}
	
	public String childXML() {
		StringBuilder childs = new StringBuilder();
		
		if(!this.getChilds().isEmpty()) {
			for(XMLObject child : this.getChilds())
				childs.append(child.asXML());
		}
		
		return childs.toString();
	}
	
	@Override
	public String toString() {
		return asXML();
	}
}
