package io.github.alphahelixdev.helius.reflection;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@EqualsAndHashCode
@ToString
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
}
