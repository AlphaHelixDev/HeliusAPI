package io.github.alphahelixdev.helius.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@EqualsAndHashCode
@ToString
public class SaveField {
	
	private final Field field;
	@Getter
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
	
	public Object get(Object instance) {
		return this.get(instance, true);
	}
	
	public Object get(Object instance, boolean stackTrace) {
		try {
			return this.asNormal().get(instance);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return new Object();
	}
	
	public Field asNormal() {
		return field;
	}
	
	public Object getStatic() {
		return this.getStatic(true);
	}
	
	public Object getStatic(boolean stackTrace) {
		return this.get(null, stackTrace);
	}
	
	public SaveField setStatic(Object value) {
		return this.set(null, value, true);
	}
	
	public SaveField set(Object instance, Object value) {
		return this.set(instance, value, true);
	}
	
	public SaveField set(Object instance, Object value, boolean stackTrace) {
		try {
			this.asNormal().set(instance, value);
		} catch(Exception e) {
			if(stackTrace) e.printStackTrace();
		}
		return this;
	}
	
	public SaveField setStatic(Object value, boolean stackTrace) {
		return this.set(null, value, stackTrace);
	}
}
