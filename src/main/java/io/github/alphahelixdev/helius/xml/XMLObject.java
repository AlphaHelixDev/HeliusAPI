package io.github.alphahelixdev.helius.xml;

import io.github.alphahelixdev.helius.xml.exceptions.NoSuchAttributeException;
import io.github.alphahelixdev.helius.xml.exceptions.NoSuchChildException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		this(null, name, attributes, new XMLObject[]{});
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
	
	public XMLObject getParent() {
		return parent;
	}
	
	public List<XMLAttribute<?>> getAttributes() {
		return this.attributes;
	}
	
	public List<XMLAttribute<?>> getDeepAttributes() {
		List<XMLAttribute<?>> attributes = getAttributes();
		
		for(XMLObject child : getChilds())
			attributes.addAll(child.getDeepAttributes());
		
		return attributes;
	}
	
	public <V> XMLAttribute<V> findAttribute(String name, Class<V> valueClass) throws NoSuchAttributeException {
		return findAttribute(name, valueClass, false);
	}
	
	public <V> XMLAttribute<V> findAttribute(String name, Class<V> valueClass, boolean deep) throws NoSuchAttributeException {
		if(deep) {
			for(XMLAttribute<?> attribute : getDeepAttributes()) {
				if(attribute.name().equals(name) && attribute.value().getClass().equals(valueClass))
					return (XMLAttribute<V>) attribute;
			}
			throw new NoSuchAttributeException(this, name);
		}
		
		for(XMLAttribute<?> attribute : getAttributes()) {
			if(attribute.name().equals(name) && attribute.value().getClass().equals(valueClass))
				return (XMLAttribute<V>) attribute;
		}
		throw new NoSuchAttributeException(this, name);
	}
	
	public List<XMLObject> getChilds() {
		return childs;
	}
	
	public XMLObject findChild(String name) throws NoSuchChildException {
		for(XMLObject child : this.childs) {
			if(child.getName().equals(name))
				return child;
		}
		throw new NoSuchChildException(this, name);
	}
	
	public String getName() {
		return name;
	}
	
	public XMLObject addChild(XMLObject child) {
		this.childs.add(child);
		return this;
	}
	
	public XMLObject addAttribute(XMLAttribute<?> attribute) {
		this.attributes.add(attribute);
		return this;
	}
	
	public String asXML() {
		return "<" + this.name + attributeXML() + ">" + childXML() + "</" + this.name + ">";
	}
	
	public String attributeXML() {
		StringBuilder attributes = new StringBuilder();
		
		if(!getAttributes().isEmpty()) {
			attributes.append(" ");
			for(XMLAttribute<?> attribute : getAttributes()) {
				attributes.append(attribute.name()).append("=\"").append(attribute.value()).append("\"").append(" ");
			}
			
			attributes.deleteCharAt(attributes.length() - 1);
		}
		
		return attributes.toString();
	}
	
	public String childXML() {
		StringBuilder childs = new StringBuilder();
		
		if(!getChilds().isEmpty()) {
			for(XMLObject child : getChilds())
				childs.append(child.asXML());
		}
		
		return childs.toString();
	}
	
	@Override
	public String toString() {
		return asXML();
	}
}

/*
<Helium>
  <month>190569550.80983543</month>
  <screen>
    <just>-979658551.006917</just>
    <list>coat</list>
    <tie>
      <loose>-249825260</loose>
      <blind>journey</blind>
      <disease>half</disease>
      <bush>gift</bush>
      <length>1153300270.674245</length>
    </tie>
    <large>1074465088.265367</large>
    <song>-1341621562.9766817</song>
    <around>867249624</around>
  </screen>
  <position>-1450508043.4075212</position>
  <best>lie</best>
  <gently>-1828973292.2084568</gently>
  <burn>review</burn>
</Helium>
 */
