package io.github.alphahelixdev.helius.xml;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class XMLStubObject<T> extends XMLObject {
	
	private T value;
	
	public XMLStubObject(String name, T value) {
		super(name);
		this.setValue(value);
	}
	
	public XMLStubObject(String name, XMLObject[] childs, T value) {
		super(name, childs);
		this.setValue(value);
	}
	
	public XMLStubObject(XMLObject parent, String name, T value) {
		super(parent, name);
		this.setValue(value);
	}
	
	public XMLStubObject(XMLObject parent, String name, XMLObject[] childs, T value) {
		super(parent, name, childs);
		this.setValue(value);
	}
	
	@Override
	public String childXML() {
		return String.valueOf(this.getValue());
	}
	
	public XMLStubObject<T> setValue(T value) {
		this.value = value;
		return this;
	}
	
	@Override
	public List<XMLObject> getChilds() {
		return ImmutableList.of();
	}
	
	@Override
	public XMLObject addChild(XMLObject child) {
		return this;
	}
}
