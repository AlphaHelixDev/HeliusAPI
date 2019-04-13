package io.github.whoisalphahelix.helius.reflection;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Constructor;

@EqualsAndHashCode
@ToString
public class SaveConstructor {
	
	private final Constructor<?> constructor;
	
	public SaveConstructor(Class<?> clazz, Class<?>... parameters) throws NoSuchMethodException {
		this(clazz.getDeclaredConstructor(parameters));
	}
	
	public SaveConstructor(Constructor<?> constructor) {
		constructor.setAccessible(true);
		this.constructor = constructor;
	}
	
	public Object newInstance(Boolean stackTrace, Object... args) {
		try {
			return this.asNormal().newInstance(args);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return null;
	}
	
	public Constructor<?> asNormal() {
		return this.constructor;
	}
}
