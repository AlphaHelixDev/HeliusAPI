package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.Constructor;
import java.util.Objects;

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
		return constructor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(constructor);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SaveConstructor that = (SaveConstructor) o;
		return Objects.equals(constructor, that.constructor);
	}

	@Override
	public String toString() {
		return "SaveConstructor{" +
				"constructor=" + constructor +
				'}';
	}
}
