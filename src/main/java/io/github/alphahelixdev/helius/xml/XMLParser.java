package io.github.alphahelixdev.helius.xml;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.xml.annotations.XML;
import io.github.alphahelixdev.helius.xml.annotations.XMLAttribute;
import io.github.alphahelixdev.helius.xml.annotations.XMLExpose;
import io.github.alphahelixdev.helius.xml.annotations.XMLProperty;
import io.github.alphahelixdev.helius.xml.exceptions.NoSuchAttributeException;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class XMLParser {
	
	public XMLObject get(String xml) {
		String[] tags = xml.split("(?=(<.+>.*</.+>|</.+>))");
		XMLObject head = new XMLObject(Helius.replaceLast(tags[0].replaceFirst("<", ""), ">", ""));
		
		for(int i = 0; i < tags.length; i++) {
			String tag = tags[i];
			
			String name = tag.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].replace("<", "").replaceAll(">.*", "");
			
			if(name.startsWith("/")) continue;
			
			String tagValue = tagValue(tagsBetween(name, tags));
			
			if(tag.matches("<.*/>")) { //is property
				XMLPropertyObject prop = new XMLPropertyObject(head, name);
				
				prop = insertAtrr(prop, tag.replaceAll("/>(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", ">"));
				
				head.addChild(prop);
			} else if(tagValue.startsWith("<") && tagValue.endsWith(">")) {
				XMLObject child = getChild(head, name, tagValue);
				
				child = insertAtrr(child, tag);
				
				if(!head.getName().equals(name)) {
					head.addChild(child);
					
					i++;
				}
			} else { //is stub
				XMLStubObject<String> stub = new XMLStubObject<>(head, name, tagValue);
				
				stub = insertAtrr(stub, tag);
				
				head.addChild(stub);
			}
		}
		
		return head;
	}
	
	private XMLObject getChild(XMLObject parent, String pName, String xml) {
		String[] tags = xml.split("(?=(<.+>.*</.+>|</.+>))");
		XMLObject head = new XMLObject(parent, pName);
		
		for(int i = 0; i < tags.length; i++) {
			String tag = tags[i];
			
			String name = tag.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].replace("<", "").replaceAll(">.*", "");
			
			if(name.startsWith("/")) continue;
			
			String tagValue = tagValue(tagsBetween(name, tags));
			
			if(tag.matches("<.*/>")) { //is property
				XMLPropertyObject prop = new XMLPropertyObject(head, name);
				
				prop = insertAtrr(prop, tag.replaceAll("/>(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", ">"));
				
				head.addChild(prop);
			} else if(tagValue.startsWith("<") && tagValue.endsWith(">")) {
				XMLObject child = getChild(head, name, tagValue);
				
				child = insertAtrr(child, tag);
				
				if(!pName.equals(name)) {
					head.addChild(child);
					i++;
				}
				
			} else { //is stub
				XMLStubObject<String> stub = new XMLStubObject<>(head, name, tagValue);
				
				stub = insertAtrr(stub, tag);
				
				head.addChild(stub);
			}
		}
		
		return head;
	}
	
	private <T extends XMLObject> T insertAtrr(T xObj, String tag) {
		if(tag.matches("<.* .*>.*")) {
			String[] tagAtrr = tagAtrrs(tag);
			
			for(int aI = 1; aI < tagAtrr.length; aI++) {
				String[] atrr = tagAtrr[aI].split("=(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
				
				xObj.addAttribute(new io.github.alphahelixdev.helius.xml.XMLAttribute<String>() {
					@Override
					public String name() {
						return atrr[0].replaceAll("\"", "");
					}
					
					@Override
					public String value() {
						return atrr[1].replaceAll("\"", "");
					}
				});
			}
		}
		return xObj;
	}
	
	private String tagValue(String tag) {
		return Helius.replaceLast(tag.replaceFirst("<.*?>", ""), "<.*?>", "");
	}
	
	private String tagsBetween(String name, String[] tags) {
		StringBuilder tillEnd = new StringBuilder();
		boolean open = false;
		
		for(String t : tags) {
			if(t.startsWith("</" + name)) open = false;
			
			if(t.startsWith("<" + name)) open = true;
			
			if(open)
				tillEnd.append(t);
		}
		
		return tillEnd.append("</").append(name).append(">").toString();
	}
	
	private String[] tagAtrrs(String tag) {
		return Helius.replaceLast(tag.replaceFirst("<.*? ", " "), ">.*", "").split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	}
	
	public XMLObject toXMLObject(XMLObject parent, String name, Object obj) {
		XMLObject head = new XMLObject(parent, name);
		head.addAttribute(new io.github.alphahelixdev.helius.xml.XMLAttribute<String>() {
			@Override
			public String name() {
				return "class";
			}
			
			@Override
			public String value() {
				return obj.getClass().getName();
			}
		});
		
		if(!obj.getClass().isAnnotationPresent(XML.class))
			return new XMLStubObject<>(head, name, obj);
		
		for(SaveField field : Helius.getReflections().getDeclaredFieldsNotAnnotated(obj.getClass(), XMLExpose.class)) {
			if(field.get(obj) == null) {
				XMLPropertyObject child = new XMLPropertyObject(head, field.asNormal().getName());
				
				if(field.asNormal().isAnnotationPresent(XMLProperty.class)) {
					XMLProperty prop = field.asNormal().getDeclaredAnnotation(XMLProperty.class);
					
					Validate.isTrue(prop.name().length == prop.value().length, "The attributes dont sum up for '" + field.asNormal().getName() + "' inside '" + obj.getClass().getSimpleName() + "'");
					
					for(int i = 0; i < prop.name().length; i++) {
						int finalI = i;
						
						child.addAttribute(new io.github.alphahelixdev.helius.xml.XMLAttribute<String>() {
							@Override
							public String name() {
								return prop.name()[finalI];
							}
							
							@Override
							public String value() {
								return prop.value()[finalI];
							}
						});
					}
				}
				head.addChild(child);
				continue;
			}
			
			XMLObject child = toXMLObject(head, field.asNormal().getName(), field.get(obj));
			
			if(field.asNormal().isAnnotationPresent(XMLAttribute.class)) {
				XMLAttribute attr = field.asNormal().getDeclaredAnnotation(XMLAttribute.class);
				
				Validate.isTrue(attr.name().length == attr.value().length, "The attributes dont sum up for '" + field.asNormal().getName() + "' inside '" + obj.getClass().getSimpleName() + "'");
				
				for(int i = 0; i < attr.name().length; i++) {
					int finalI = i;
					
					child.addAttribute(new io.github.alphahelixdev.helius.xml.XMLAttribute<String>() {
						@Override
						public String name() {
							return attr.name()[finalI];
						}
						
						@Override
						public String value() {
							return attr.value()[finalI];
						}
					});
				}
			}
			
			head.addChild(child);
		}
		
		return head;
	}
	
	public Object fromXMLObject(XMLObject obj) throws ReflectiveOperationException, NoSuchAttributeException {
		Class<?> tClass = Class.forName(obj.findAttribute("class", String.class).value());
		Constructor<?> con = tClass.getDeclaredConstructor();
		con.setAccessible(true);
		Object type = con.newInstance();
		
		for(XMLObject cObj : obj.getChilds()) {
			Field f = tClass.getDeclaredField(cObj.getName());
			f.setAccessible(true);
			
			if(cObj instanceof XMLPropertyObject) continue;
			
			if((f.getType().isPrimitive() || f.getType().equals(String.class)) && cObj instanceof XMLStubObject)
				f.set(type, ((XMLStubObject) cObj).getValue());
			else
				f.set(type, fromXMLObject(cObj));
		}
		
		return type;
	}
}