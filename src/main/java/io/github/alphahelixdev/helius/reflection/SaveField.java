package io.github.alphahelixdev.helius.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SaveField {
	
	private final Field field;
	private final int classIndex;
	
	public SaveField(Field field) {
		this(field, 0);
	}
	
	public SaveField(Field field, int classIndex) {
		this.field = field;
		this.field.setAccessible(true);
		this.classIndex = classIndex;
	}
	
	public SaveField removeFinal() {
		try {
			if(Modifier.isFinal(asNormal().getModifiers())) {
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(asNormal(), asNormal().getModifiers() & ~Modifier.FINAL);
			}
		} catch(IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Field asNormal() {
		return field;
	}
	
	public Object get(Object instance) {
		return get(instance, true);
	}
	
	public Object get(Object instance, boolean stackTrace) {
		try {
			return field.get(instance);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return new Object();
	}
	
	public Object getStatic() {
		return getStatic(true);
	}
	
	public Object getStatic(boolean stackTrace) {
		return get(null, stackTrace);
	}
	
	public SaveField setStatic(Object value) {
		return set(null, value, true);
	}
	
	public SaveField set(Object instance, Object value, boolean stackTrace) {
		try {
			field.set(instance, value);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return this;
	}
	
	public SaveField set(Object instance, Object value) {
		return set(instance, value, true);
	}
	
	public SaveField setStatic(Object value, boolean stackTrace) {
		return set(null, value, stackTrace);
	}
	
	public int getClassIndex() {
		return classIndex;
	}
	
	@Override
	public String toString() {
		return "SaveField{" +
				"field=" + field +
				", classIndex=" + classIndex +
				'}';
	}
}
