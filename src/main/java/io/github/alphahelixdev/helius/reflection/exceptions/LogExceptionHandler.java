package io.github.alphahelixdev.helius.reflection.exceptions;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

import java.util.logging.Level;

public class LogExceptionHandler implements ExceptionHandler {
	@Override
	public SaveMethod noSuchMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + name + "(" + this.getClassArrayString(parameterClasses) + ") inside " + where.getName());
		return null;
	}
	
	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + name + "(" + this.getClassArrayString(parameterClasses) + ") inside " + where.getName());
		return null;
	}
	
	@Override
	public SaveConstructor<?> noSuchConstructor(Class<?> where, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + where.getName() + "(" + this.getClassArrayString(parameterClasses) + ")");
		return null;
	}
	
	@Override
	public SaveConstructor<?> noSuchPrivateConstructor(Class<?> where, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + where.getName() + "(" + this.getClassArrayString(parameterClasses) + ")");
		return null;
	}
	
	@Override
	public SaveField noSuchField(Class<?> where, String name) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + name + " inside " + where.getName());
		return null;
	}
	
	@Override
	public SaveField noSuchField(Class<?> where, Class<?> type) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find type " + type.getName() + " inside " + where.getName());
		return null;
	}
	
	@Override
	public SaveField noSuchPrivateField(Class<?> where, String name) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + name + " inside " + where.getName());
		return null;
	}
	
	@Override
	public SaveField noSuchPrivateField(Class<?> where, Class<?> type) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private type " + type.getName() + " inside " + where.getName());
		return null;
	}
	
	@Override
	public Class<?> noSuchClass(String name) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + name);
		return null;
	}
	
	private String getClassArrayString(Class<?>... classes) {
		StringBuilder builder = new StringBuilder();
		
		for(Class<?> clazz : classes)
			builder.append(clazz.getName()).append(", ");
		
		return builder.toString();
	}
}
