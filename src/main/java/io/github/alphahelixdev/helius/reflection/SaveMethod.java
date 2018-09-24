package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SaveMethod {
	
	private final Method method;
	
	public SaveMethod(Method method) {
		this.method = method;
		method.setAccessible(true);
	}
	
	public Object invoke(Object instance, Object... arguments) {
		return invoke(instance, true, arguments);
	}
	
	public Object invoke(Object instance, boolean stack, Object... arguments) {
		try {
			return method.invoke(instance, arguments);
		} catch(IllegalAccessException | InvocationTargetException e) {
			if(stack) e.printStackTrace();
			return null;
		}
	}
	
	public Object invokeStatic(Object... arguments) {
		return invoke(null, true, arguments);
	}
	
	public Object invokeStatic(boolean stack, Object... arguments) {
		return invoke(null, stack, arguments);
	}
	
	public Method asNormal() {
		return method;
	}
}
