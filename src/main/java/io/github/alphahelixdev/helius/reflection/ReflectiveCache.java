package io.github.alphahelixdev.helius.reflection;

import io.github.alphahelixdev.helius.Cache;
import io.github.alphahelixdev.helius.Helius;
import org.apache.commons.collections4.map.MultiKeyMap;

public class ReflectiveCache implements Cache {
	
	private final MultiKeyMap<Object, SaveMethod> methodMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveField> fieldMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveConstructor<?>> constructorMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, Class<?>> classMap = new MultiKeyMap<>();
	
	public ReflectiveCache() {
		Helius.addCache(this);
	}
	
	public MultiKeyMap<Object, SaveMethod> methods() {
		return methodMap;
	}
	
	public MultiKeyMap<Object, SaveField> fields() {
		return fieldMap;
	}
	
	public MultiKeyMap<Object, SaveConstructor<?>> constructors() {
		return constructorMap;
	}
	
	public MultiKeyMap<Object, Class<?>> classes() {
		return classMap;
	}
	
	@Override
	public void clear() {
		methodMap.clear();
		fieldMap.clear();
		constructorMap.clear();
		classMap.clear();
	}
	
	@Override
	public String clearMessage() {
		return "Reflection Cache cleared";
	}
}
