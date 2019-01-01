package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class SaveMethod {
	
	private final Method method;
	
	public SaveMethod(Method method) {
		this.method = method;
		method.setAccessible(true);
	}
	
	public Object invokeStatic(Object... arguments) {
		return this.invoke(null, true, arguments);
	}
	
	public Object invoke(Object instance, boolean stack, Object... arguments) {
		try {
			return this.asNormal().invoke(instance, arguments);
		} catch(IllegalAccessException | InvocationTargetException e) {
			if(stack) e.printStackTrace();
			return null;
		}
	}
	
	public Object invokeStatic(boolean stack, Object... arguments) {
		return this.invoke(null, stack, arguments);
	}
	
	public Method asNormal() {
		return method;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.method);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SaveMethod that = (SaveMethod) o;
		return Objects.equals(this.method, that.method);
	}
	
	@Override
	public String toString() {
		return "SaveMethod{" +
				"method=" + this.method +
				'}';
	}
}
