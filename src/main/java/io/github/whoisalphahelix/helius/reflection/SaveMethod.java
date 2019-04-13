package io.github.whoisalphahelix.helius.reflection;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

@EqualsAndHashCode(callSuper = false)
@ToString
public class SaveMethod extends AccessibleObject {
	
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
	
	public Method asNormal() {
		return method;
	}
	
	public Object invokeStatic(boolean stack, Object... arguments) {
		return this.invoke(null, stack, arguments);
	}
	
	public int getParameterCount() {
		return asNormal().getParameterCount();
	}
	
	public Type[] getGenericParameterTypes() {
		return asNormal().getGenericParameterTypes();
	}
	
	public Parameter[] getParameters() {
		return asNormal().getParameters();
	}
	
	public Type[] getGenericExceptionTypes() {
		return asNormal().getGenericExceptionTypes();
	}
	
	public boolean isVarArgs() {
		return asNormal().isVarArgs();
	}
	
	public boolean isSynthetic() {
		return asNormal().isSynthetic();
	}
	
	public AnnotatedType getAnnotatedReceiverType() {
		return asNormal().getAnnotatedReceiverType();
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return asNormal().getAnnotation(annotationClass);
	}
	
	public AnnotatedType[] getAnnotatedParameterTypes() {
		return asNormal().getAnnotatedParameterTypes();
	}
	
	@Override
	public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
		return asNormal().getAnnotationsByType(annotationClass);
	}
	
	public AnnotatedType[] getAnnotatedExceptionTypes() {
		return asNormal().getAnnotatedExceptionTypes();
	}
	
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return asNormal().getDeclaredAnnotations();
	}
	
	public Class<?> getDeclaringClass() {
		return asNormal().getDeclaringClass();
	}
	
	public String getName() {
		return asNormal().getName();
	}
	
	public int getModifiers() {
		return asNormal().getModifiers();
	}
	
	public TypeVariable<?>[] getTypeParameters() {
		return asNormal().getTypeParameters();
	}
	
	@Override
	public void setAccessible(boolean flag) throws SecurityException {
		asNormal().setAccessible(flag);
	}
	
	public Class<?>[] getParameterTypes() {
		return asNormal().getParameterTypes();
	}
	
	@Override
	public boolean isAccessible() {
		return asNormal().isAccessible();
	}
	
	public Class<?>[] getExceptionTypes() {
		return asNormal().getExceptionTypes();
	}
	
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return asNormal().isAnnotationPresent(annotationClass);
	}
	
	public String toGenericString() {
		return asNormal().toGenericString();
	}
	
	@Override
	public Annotation[] getAnnotations() {
		return asNormal().getAnnotations();
	}
	
	public Annotation[][] getParameterAnnotations() {
		return asNormal().getParameterAnnotations();
	}
	
	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
		return asNormal().getDeclaredAnnotation(annotationClass);
	}
	
	public AnnotatedType getAnnotatedReturnType() {
		return asNormal().getAnnotatedReturnType();
	}
	
	@Override
	public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
		return asNormal().getDeclaredAnnotationsByType(annotationClass);
	}
	
	public Class<?> getReturnType() {
		return asNormal().getReturnType();
	}
	
	public Type getGenericReturnType() {
		return asNormal().getGenericReturnType();
	}
	
	public boolean isBridge() {
		return asNormal().isBridge();
	}
	
	public boolean isDefault() {
		return asNormal().isDefault();
	}
	
	public Object getDefaultValue() {
		return asNormal().getDefaultValue();
	}
	
	
}
