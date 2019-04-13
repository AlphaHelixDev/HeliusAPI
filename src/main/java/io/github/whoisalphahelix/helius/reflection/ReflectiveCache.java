package io.github.whoisalphahelix.helius.reflection;

import io.github.whoisalphahelix.helius.Cache;
import io.github.whoisalphahelix.helius.Helius;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections4.map.MultiKeyMap;

@EqualsAndHashCode
@ToString
public class ReflectiveCache implements Cache {
	
	private final MultiKeyMap<Object, SaveMethod> methodMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveMethod> privateMethodMap = new MultiKeyMap<>();
	
	private final MultiKeyMap<Object, SaveField> fieldMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveField> privateFieldMap = new MultiKeyMap<>();

	private final MultiKeyMap<Object, SaveConstructor> constructorMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveConstructor> privateConstructorMap = new MultiKeyMap<>();
	
	private final MultiKeyMap<Object, Class<?>> classMap = new MultiKeyMap<>();
	
	public ReflectiveCache() {
		Helius.addCache(this);
	}
	
	@Override
	public void clear() {
		this.methods().clear();
		this.privateMethods().clear();
		
		this.fields().clear();
		this.privateFields().clear();
		
		this.constructors().clear();
		this.privateConstructors().clear();
		
		this.classes().clear();
	}
	
	public MultiKeyMap<Object, SaveMethod> methods() {
		return this.methodMap;
	}
	
	public MultiKeyMap<Object, SaveMethod> privateMethods() {
		return this.privateMethodMap;
	}
	
	public MultiKeyMap<Object, SaveField> fields() {
		return this.fieldMap;
	}
	
	public MultiKeyMap<Object, SaveField> privateFields() {
		return this.privateFieldMap;
	}

	public MultiKeyMap<Object, SaveConstructor> constructors() {
		return this.constructorMap;
	}

	public MultiKeyMap<Object, SaveConstructor> privateConstructors() {
		return this.privateConstructorMap;
	}
	
	public MultiKeyMap<Object, Class<?>> classes() {
		return this.classMap;
	}
	
	@Override
	public String clearMessage() {
		return "Reflection Cache cleared";
	}
}
