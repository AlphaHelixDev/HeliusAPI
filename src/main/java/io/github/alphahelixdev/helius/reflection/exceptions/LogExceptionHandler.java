package io.github.alphahelixdev.helius.reflection.exceptions;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LogExceptionHandler implements ExceptionHandler {
	@Override
	public SaveMethod noSuchMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + name +
				"(" + this.getClassArrayString(parameterClasses) + ") inside " + where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, Class<?> type) {
		Helius.getLog().log(Level.SEVERE, "Unable to find method returning " + type.getName() + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, Class<?>... parameterTypes) {
		Helius.getLog().log(Level.SEVERE, "Unable to find method with parameters "
				+ Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.toList()) + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchMethod(Class<?> where, String name) {
		Helius.getLog().log(Level.SEVERE, "Unable to find method with name " + name + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, String name, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + name + "("
				+ this.getClassArrayString(parameterClasses) + ") inside " + where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, Class<?> type) {
		Helius.getLog().log(Level.SEVERE, "Unable to find private method returning " + type.getName() + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, Class<?>... parameterTypes) {
		Helius.getLog().log(Level.SEVERE, "Unable to find private method with parameters "
				+ Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.toList()) + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveMethod noSuchPrivateMethod(Class<?> where, String name) {
		Helius.getLog().log(Level.SEVERE, "Unable to find private method with name " + name + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveConstructor noSuchConstructor(Class<?> where, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + where.getName() + "("
				+ this.getClassArrayString(parameterClasses) + ")");
		return null;
	}

	@Override
	public SaveConstructor noSuchPrivateConstructor(Class<?> where, Class<?>... parameterClasses) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + where.getName() + "("
				+ this.getClassArrayString(parameterClasses) + ")");
		return null;
	}

	@Override
	public SaveField noSuchField(Class<?> where, String name) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find " + name + " inside " + where.getName());
		return null;
	}

	@Override
	public SaveField noSuchField(Class<?> where, Class<?> type) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find type " + type.getName() + " inside "
				+ where.getName());
		return null;
	}

	@Override
	public SaveField noSuchPrivateField(Class<?> where, String name) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private " + name + " inside " + where.getName());
		return null;
	}

	@Override
	public SaveField noSuchPrivateField(Class<?> where, Class<?> type) {
		Helius.getLogger().log(Level.SEVERE, "Unable to find private type " + type.getName() + " inside "
				+ where.getName());
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
