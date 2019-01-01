package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class SaveConstructor<T> {
	
	private final Constructor<T> constructor;
	
	public SaveConstructor(Class<T> clazz, Class<?>... parameters) throws NoSuchMethodException {
		this(clazz.getDeclaredConstructor(parameters));
	}
	
	public SaveConstructor(Constructor<T> constructor) {
		constructor.setAccessible(true);
		this.constructor = constructor;
	}
	
	public T newInstance(Boolean stackTrace, Object... args) {
		try {
			return this.asNormal().newInstance(args);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return null;
	}
	
	public Constructor<T> asNormal() {
		return constructor;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.constructor);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SaveConstructor<?> that = (SaveConstructor<?>) o;
		return Objects.equals(this.constructor, that.constructor);
	}
	
	@Override
	public String toString() {
		return "SaveConstructor{" +
				"                            constructor=" + this.constructor +
				'}';
	}
}
