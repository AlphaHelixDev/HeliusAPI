package io.github.alphahelixdev.helius.reflection.exceptions;

import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

public class NullExceptionHandler implements ExceptionHandler {
	
	@Override
	public SaveMethod noSuchMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, Class<?> type) {
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, Class<?>... parameterTypes) {
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, String name) {
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, Class<?> type) {
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, Class<?>... parameterTypes) {
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, String name) {
		return null;
	}

	@Override
	public SaveConstructor noSuchConstructor(Class<?> where, Class<?>... parameterClasses) {
		return null;
	}

	@Override
	public SaveConstructor noSuchPrivateConstructor(Class<?> where, Class<?>... parameterClasses) {
		return null;
	}
	
	@Override
	public SaveField noSuchField(Class<?> where, String name) {
		return null;
	}
	
	@Override
	public SaveField noSuchField(Class<?> where, Class<?> type) {
		return null;
	}
	
	@Override
	public SaveField noSuchPrivateField(Class<?> where, String name) {
		return null;
	}
	
	@Override
	public SaveField noSuchPrivateField(Class<?> where, Class<?> type) {
		return null;
	}
	
	@Override
	public Class<?> noSuchClass(String name) {
		return null;
	}
	
	@Override
	public String toString() {
		return "NullExceptionHandler{}";
	}
}
