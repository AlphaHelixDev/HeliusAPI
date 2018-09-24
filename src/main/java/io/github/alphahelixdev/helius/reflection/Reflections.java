package io.github.alphahelixdev.helius.reflection;


import io.github.alphahelixdev.helius.Helius;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Reflections {
	
	private final ReflectiveCache cache = new ReflectiveCache();
	
	public Reflections() {
	}
	
	//####################################### Fields ###########################################
	
	public SaveField getField(String name, Class<?> clazz) throws NoSuchFieldException {
		Field f = clazz.getField(name);
		return new SaveField(f);
	}
	
	public SaveField getDeclaredField(String name, Class<?> clazz) throws NoSuchFieldException {
		Field f = clazz.getDeclaredField(name);
		return new SaveField(f);
	}
	
	public List<SaveField> getFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : getFields(clazz)) {
			if(f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getFields(Class<?> clazz) {
		List<SaveField> fields = new LinkedList<>();
		Field[] fs = clazz.getFields();
		
		for(int i = 0; i < fs.length; i++) {
			fields.add(new SaveField(fs[i], i));
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : getDeclaredFields(clazz)) {
			if(f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFields(Class<?> clazz) {
		List<SaveField> fields = new LinkedList<>();
		Field[] fs = clazz.getDeclaredFields();
		
		for(int i = 0; i < fs.length; i++) {
			fields.add(new SaveField(fs[i], i));
		}
		
		return fields;
	}
	
	public List<SaveField> getFieldsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : getFields(clazz)) {
			if(!f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFieldsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : getDeclaredFields(clazz)) {
			if(!f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public SaveField getFirstFieldWithType(Class<?> type, Class<?> clazz) throws NoSuchFieldException {
		for(SaveField field : getFields(clazz)) {
			if(field.asNormal().getType().equals(type)) {
				return field;
			}
		}
		
		throw new NoSuchFieldException("Could not resolve public field of type '" + type.toString() + "' in class " + clazz);
	}
	
	public SaveField getLastFieldWithType(Class<?> type, Class<?> clazz) throws NoSuchFieldException {
		SaveField field = null;
		for(SaveField field1 : getFields(clazz)) {
			if(field1.asNormal().getType().equals(type)) {
				field = field1;
			}
		}
		
		if(field == null)
			throw new NoSuchFieldException("Could not resolve public field of type '" + type.toString() + "' in class " + clazz);
		return field;
	}
	
	public SaveField getFirstDeclaredFieldWithType(Class<?> type, Class<?> clazz) throws NoSuchFieldException {
		for(SaveField field : getDeclaredFields(clazz)) {
			if(field.asNormal().getType().equals(type)) {
				return field;
			}
		}
		
		throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
	}
	
	public SaveField getLastDeclaredFieldWithType(Class<?> type, Class<?> clazz) throws NoSuchFieldException {
		SaveField field = null;
		for(SaveField field1 : getDeclaredFields(clazz)) {
			if(field1.asNormal().getType().equals(type)) {
				field = field1;
			}
		}
		
		if(field == null)
			throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
		return field;
	}
	
	//####################################### Methods ###########################################
	
	public List<SaveMethod> getMethods(Class<?> clazz) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Method m : clazz.getMethods())
			methods.add(new SaveMethod(m));
		
		return methods;
	}
	
	public List<SaveMethod> getDeclaredMethods(Class<?> clazz) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Method m : clazz.getDeclaredMethods())
			methods.add(new SaveMethod(m));
		
		return methods;
	}
	
	public SaveMethod getMethod(String name, Class<?> clazz, Class<?>... parameterClasses) throws NoSuchMethodException {
		if(cache.methods().containsKey(name, clazz, parameterClasses))
			return cache.methods().get(name, clazz, parameterClasses);
		
		SaveMethod sm = new SaveMethod(clazz.getMethod(name, parameterClasses));
		
		cache.methods().put(name, clazz, parameterClasses, sm);
		
		return sm;
	}
	
	public SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses) throws NoSuchMethodException {
		if(cache.methods().containsKey(name, clazz, parameterClasses))
			return cache.methods().get(name, clazz, parameterClasses);
		
		SaveMethod sm = new SaveMethod(clazz.getDeclaredMethod(name, parameterClasses));
		
		cache.methods().put(name, clazz, parameterClasses, sm);
		
		return sm;
	}
	
	//####################################### Classes ###########################################
	
	public Class<?> getClass(String name, boolean asArray) throws ClassNotFoundException {
		if(cache.classes().containsKey(name, asArray))
			return cache.classes().get(name, asArray);
		
		if(asArray) {
			Class<?> arrayClazz = Array.newInstance(Class.forName(name), 0).getClass();
			
			cache.classes().put(name, true, arrayClazz);
			
			return arrayClazz;
		} else {
			Class<?> clazz = Class.forName(name);
			
			cache.classes().put(name, false, clazz);
			
			return clazz;
		}
	}
	
	public Set<Class<?>> findClassesAnnotated(Class<? extends Annotation> annotation, Class<?>... classes) {
		Set<Class<?>> clazzSet = new HashSet<>();
		
		for(Class<?> clazz : classes) {
			if(clazz.isAnnotationPresent(annotation) && !annotation.equals(clazz))
				clazzSet.add(clazz);
		}
		
		return clazzSet;
	}
	
	public Class<?>[] findClassesImplementing(Class<?> interfaze, Class<?>... classes) {
		List<Class<?>> clazzSet = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			if(interfaze.isAssignableFrom(clazz) && !interfaze.equals(clazz))
				clazzSet.add(clazz);
		}
		
		return clazzSet.toArray(new Class<?>[clazzSet.size()]);
	}
	
	public Class<?>[] getClassesFromFolder(File folder) {
		File[] jars = folder.listFiles();
		
		if(jars != null) {
			for(File jar : jars) {
				if(jar.getName().endsWith(".jar")) {
					return getClassesFromJar(jar);
				}
			}
		}
		return new Class<?>[]{};
	}
	
	public Class<?>[] getClassesFromJar(File jarFile) {
		List<Class<?>> classes = new LinkedList<>();
		
		try {
			JarFile file = new JarFile(jarFile);
			
			for(Enumeration<JarEntry> entries = file.entries(); entries.hasMoreElements(); ) {
				JarEntry entry = entries.nextElement();
				String jarName = entry.getName().replace('/', '.');
				
				if(jarName.endsWith(".class")) {
					String clName = jarName.substring(0, jarName.length() - 6);
					
					classes.add(Helius.class.getClassLoader().loadClass(clName));
				}
			}
			file.close();
			
		} catch(IOException | ReflectiveOperationException ex) {
			Helius.getLogger().severe("Unable to get class, log: " + ex);
			ex.printStackTrace();
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
}
