package io.github.alphahelixdev.helius.reflection.exceptions;

import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

public interface ExceptionHandler {

	SaveMethod noSuchMethod(Class<?> where, String name, Class<?>... parameterTypes);

	SaveMethod noSuchMethod(Class<?> where, Class<?> type);

	SaveMethod noSuchMethod(Class<?> where, Class<?>... parameterTypes);

	SaveMethod noSuchMethod(Class<?> where, String name);

	SaveMethod noSuchPrivateMethod(Class<?> where, String name, Class<?>... parameterTypes);

	SaveMethod noSuchPrivateMethod(Class<?> where, Class<?> type);

	SaveMethod noSuchPrivateMethod(Class<?> where, Class<?>... parameterTypes);

	SaveMethod noSuchPrivateMethod(Class<?> where, String name);

	SaveConstructor noSuchConstructor(Class<?> where, Class<?>... parameterTypes);

	SaveConstructor noSuchPrivateConstructor(Class<?> where, Class<?>... parameterTypes);
	
	SaveField noSuchField(Class<?> where, String name);
	
	SaveField noSuchField(Class<?> where, Class<?> type);
	
	SaveField noSuchPrivateField(Class<?> where, String name);
	
	SaveField noSuchPrivateField(Class<?> where, Class<?> type);
	
	Class<?> noSuchClass(String name);
	
}
