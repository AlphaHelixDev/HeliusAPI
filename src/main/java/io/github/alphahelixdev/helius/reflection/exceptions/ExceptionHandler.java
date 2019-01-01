package io.github.alphahelixdev.helius.reflection.exceptions;

import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

public interface ExceptionHandler {
	
	SaveMethod noSuchMethod(Class<?> where, String name, Class<?>... parameterClasses);
	
	SaveMethod noSuchPrivateMethod(Class<?> where, String name, Class<?>... parameterClasses);
	
	SaveConstructor<?> noSuchConstructor(Class<?> where, Class<?>... parameterClasses);
	
	SaveConstructor<?> noSuchPrivateConstructor(Class<?> where, Class<?>... parameterClasses);
	
	SaveField noSuchField(Class<?> where, String name);
	
	SaveField noSuchField(Class<?> where, Class<?> type);
	
	SaveField noSuchPrivateField(Class<?> where, String name);
	
	SaveField noSuchPrivateField(Class<?> where, Class<?> type);
	
	Class<?> noSuchClass(String name);
	
}
