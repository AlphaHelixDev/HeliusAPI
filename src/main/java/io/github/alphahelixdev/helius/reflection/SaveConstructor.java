package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.Constructor;

public class SaveConstructor<T> {
	
	private T type;
	private Constructor<T> constructor;
	
	public SaveConstructor(Class<T> clazz, Class<?>... parameters) throws NoSuchMethodException {
		this(clazz.getDeclaredConstructor(parameters));
	}
	
	public SaveConstructor(Constructor<T> constructor) {
		constructor.setAccessible(true);
		this.constructor = constructor;
	}
	
	public T newInstance(Boolean stackTrace, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return null;
	}
	
	public Constructor<T> asNormal() {
		return constructor;
	}
}
