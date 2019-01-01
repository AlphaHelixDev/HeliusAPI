package io.github.alphahelixdev.helius.reflection;

import io.github.alphahelixdev.helius.Cache;
import io.github.alphahelixdev.helius.Helius;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.Objects;

public class ReflectiveCache implements Cache {
	
	private final MultiKeyMap<Object, SaveMethod> methodMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveMethod> privateMethodMap = new MultiKeyMap<>();
	
	private final MultiKeyMap<Object, SaveField> fieldMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveField> privateFieldMap = new MultiKeyMap<>();
	
	private final MultiKeyMap<Object, SaveConstructor<?>> constructorMap = new MultiKeyMap<>();
	private final MultiKeyMap<Object, SaveConstructor<?>> privateConstructorMap = new MultiKeyMap<>();
	
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
	
	public MultiKeyMap<Object, SaveConstructor<?>> constructors() {
		return this.constructorMap;
	}
	
	public MultiKeyMap<Object, SaveConstructor<?>> privateConstructors() {
		return this.privateConstructorMap;
	}
	
	public MultiKeyMap<Object, Class<?>> classes() {
		return this.classMap;
	}
	
	@Override
	public String clearMessage() {
		return "Reflection Cache cleared";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.methodMap, this.privateMethodMap, this.fieldMap, this.privateFieldMap, this.constructorMap, this.privateConstructorMap, this.classMap);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ReflectiveCache that = (ReflectiveCache) o;
		return Objects.equals(this.methods(), that.methods()) &&
				Objects.equals(this.privateMethods(), that.privateMethods()) &&
				Objects.equals(this.fields(), that.fields()) &&
				Objects.equals(this.privateFields(), that.privateFields()) &&
				Objects.equals(this.constructors(), that.constructors()) &&
				Objects.equals(this.privateConstructors(), that.privateConstructors()) &&
				Objects.equals(this.classes(), that.classes());
	}
	
	@Override
	public String toString() {
		return "ReflectiveCache{" +
				"                            methodMap=" + this.methodMap +
				",                             privateMethodMap=" + this.privateMethodMap +
				",                             fieldMap=" + this.fieldMap +
				",                             privateFieldMap=" + this.privateFieldMap +
				",                             constructorMap=" + this.constructorMap +
				",                             privateConstructorMap=" + this.privateConstructorMap +
				",                             classMap=" + this.classMap +
				'}';
	}
}
