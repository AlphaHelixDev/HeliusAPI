package io.github.whoisalphahelix.helius.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

@EqualsAndHashCode(callSuper = false)
@ToString
public class SaveField extends AccessibleObject implements Member {
	
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
	
	public SaveField setStatic(Object value, boolean stackTrace) {
		return this.set(null, value, stackTrace);
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
	
	@Override
	public Class<?> getDeclaringClass() {
		return asNormal().getDeclaringClass();
	}
	
	@Override
	public void setAccessible(boolean flag) throws SecurityException {
		asNormal().setAccessible(flag);
	}
	
	@Override
	public String getName() {
		return asNormal().getName();
	}
	
	@Override
	public boolean isAccessible() {
		return asNormal().isAccessible();
	}
	
	@Override
	public int getModifiers() {
		return asNormal().getModifiers();
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return asNormal().getAnnotation(annotationClass);
	}
	
	@Override
	public boolean isSynthetic() {
		return asNormal().isSynthetic();
	}
	
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return asNormal().isAnnotationPresent(annotationClass);
	}
	
	@Override
	public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
		return asNormal().getAnnotationsByType(annotationClass);
	}
	
	@Override
	public Annotation[] getAnnotations() {
		return asNormal().getAnnotations();
	}
	
	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
		return asNormal().getDeclaredAnnotation(annotationClass);
	}
	
	@Override
	public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
		return asNormal().getDeclaredAnnotationsByType(annotationClass);
	}
	
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return asNormal().getDeclaredAnnotations();
	}
	

	

	

	

}
