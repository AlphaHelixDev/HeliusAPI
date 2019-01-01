package io.github.alphahelixdev.helius.reflection;


import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.exceptions.ExceptionHandler;
import io.github.alphahelixdev.helius.reflection.exceptions.NullExceptionHandler;

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
	private ExceptionHandler exceptionHandler;
	
	public Reflections() {
		this.setExceptionHandler(new NullExceptionHandler());
	}
	
	//####################################### Fields ###########################################
	
	public SaveField getField(String name, Class<?> clazz) {
		if(this.getCache().fields().containsKey(name, clazz))
			return this.getCache().fields().get(name, clazz);
		
		try {
			SaveField f = new SaveField(clazz.getField(name));
			
			this.getCache().fields().put(name, clazz, f);
			
			return f;
		} catch(NoSuchFieldException e) {
			return this.getExceptionHandler().noSuchField(clazz, name);
		}
	}
	
	public ReflectiveCache getCache() {
		return this.cache;
	}
	
	public ExceptionHandler getExceptionHandler() {
		return this.exceptionHandler;
	}
	
	public Reflections setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}
	
	public SaveField getDeclaredField(String name, Class<?> clazz) {
		if(this.getCache().privateFields().containsKey(name, clazz))
			return this.getCache().privateFields().get(name, clazz);
		
		try {
			SaveField f = new SaveField(clazz.getDeclaredField(name));
			
			this.getCache().privateFields().put(name, clazz, f);
			
			return f;
		} catch(NoSuchFieldException e) {
			return this.getExceptionHandler().noSuchPrivateField(clazz, name);
		}
	}
	
	public List<SaveField> getFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : this.getFields(clazz)) {
			if(f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getFields(Class<?> clazz) {
		List<SaveField> fields = new LinkedList<>();
		Field[] fs = clazz.getFields();
		
		for(int i = 0; i < fs.length; i++) {
			SaveField f = new SaveField(fs[i], i);
			
			this.getCache().fields().put(f.asNormal().getName(), clazz, f);
			
			fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : this.getDeclaredFields(clazz)) {
			if(f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFields(Class<?> clazz) {
		List<SaveField> fields = new LinkedList<>();
		Field[] fs = clazz.getDeclaredFields();
		
		for(int i = 0; i < fs.length; i++) {
			SaveField f = new SaveField(fs[i], i);
			
			this.getCache().privateFields().put(f.asNormal().getName(), clazz, f);
			
			fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getFieldsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : this.getFields(clazz)) {
			if(!f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public List<SaveField> getDeclaredFieldsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<SaveField> fields = new LinkedList<>();
		
		for(SaveField f : this.getDeclaredFields(clazz)) {
			if(!f.asNormal().isAnnotationPresent(annotation))
				fields.add(f);
		}
		
		return fields;
	}
	
	public SaveField getFirstFieldWithType(Class<?> type, Class<?> clazz) {
		for(SaveField field : this.getFields(clazz)) {
			if(field.asNormal().getType().equals(type)) {
				return field;
			}
		}
		
		return this.getExceptionHandler().noSuchField(clazz, type);
	}
	
	//####################################### Methods ###########################################
	
	public SaveField getLastFieldWithType(Class<?> type, Class<?> clazz) {
		SaveField field = null;
		for(SaveField field1 : this.getFields(clazz)) {
			if(field1.asNormal().getType().equals(type)) {
				field = field1;
			}
		}
		
		if(field == null)
			return this.getExceptionHandler().noSuchField(clazz, type);
		return field;
	}
	
	public SaveField getFirstDeclaredFieldWithType(Class<?> type, Class<?> clazz) {
		for(SaveField field : this.getDeclaredFields(clazz)) {
			if(field.asNormal().getType().equals(type)) {
				return field;
			}
		}
		
		return this.getExceptionHandler().noSuchPrivateField(clazz, type);
	}
	
	public SaveField getLastDeclaredFieldWithType(Class<?> type, Class<?> clazz) {
		SaveField field = null;
		for(SaveField field1 : this.getDeclaredFields(clazz)) {
			if(field1.asNormal().getType().equals(type)) {
				field = field1;
			}
		}
		
		if(field == null)
			return this.getExceptionHandler().noSuchPrivateField(clazz, type);
		
		return field;
	}
	
	public List<SaveMethod> getMethods(Class<?> clazz) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Method m : clazz.getMethods()) {
			SaveMethod sm = new SaveMethod(m);
			
			this.getCache().methods().put(m.getName(), clazz, m.getParameterTypes(), sm);
			
			methods.add(sm);
		}
		
		return methods;
	}
	
	//####################################### Classes ###########################################
	
	public List<SaveMethod> getDeclaredMethods(Class<?> clazz) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Method m : clazz.getDeclaredMethods()) {
			SaveMethod sm = new SaveMethod(m);
			
			this.getCache().privateMethods().put(m.getName(), clazz, m.getParameterTypes(), sm);
			
			methods.add(sm);
		}
		return methods;
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
	
	public SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
		if(this.getCache().privateMethods().containsKey(name, clazz, parameterClasses))
			return this.getCache().privateMethods().get(name, clazz, parameterClasses);
		
		try {
			SaveMethod sm = new SaveMethod(clazz.getDeclaredMethod(name, parameterClasses));
			
			this.getCache().privateMethods().put(name, clazz, parameterClasses, sm);
			
			return sm;
		} catch(NoSuchMethodException e) {
			return this.getExceptionHandler().noSuchPrivateMethod(clazz, name, parameterClasses);
		}
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
	
	//####################################### Constructors ###########################################
	
	public Class<?> getClass(String name, boolean asArray) {
		if(this.getCache().classes().containsKey(name, asArray))
			return this.getCache().classes().get(name, asArray);
		
		try {
			if(asArray) {
				Class<?> arrayClazz = Array.newInstance(Class.forName(name), 0).getClass();
				
				this.getCache().classes().put(name, true, arrayClazz);
				
				return arrayClazz;
			} else {
				Class<?> clazz = Class.forName(name);
				
				this.getCache().classes().put(name, false, clazz);
				
				return clazz;
			}
		} catch(ClassNotFoundException e) {
			return this.getExceptionHandler().noSuchClass(name);
		}
	}
	
	public Class<?>[] getClassesFromFolder(File folder) {
		File[] jars = folder.listFiles();
		
		if(jars != null) {
			for(File jar : jars) {
				if(jar.getName().endsWith(".jar")) {
					return this.getClassesFromJar(jar);
				}
			}
		}
		
		return new Class<?>[]{};
	}
	
	//####################################### Random Shit ###########################################
	
	public SaveConstructor getConstructor(Class<?> clazz, Class<?>... parameterClasses) {
		if(this.getCache().constructors().containsKey(clazz, parameterClasses))
			return this.getCache().constructors().get(clazz, parameterClasses);
		
		try {
			SaveConstructor sc = new SaveConstructor<>(clazz.getConstructor(parameterClasses));
			
			this.getCache().constructors().put(clazz, parameterClasses, sc);
			
			return sc;
		} catch(NoSuchMethodException e) {
			return this.getExceptionHandler().noSuchConstructor(clazz, parameterClasses);
		}
	}
	
	public SaveConstructor getDeclaredConstructor(Class<?> clazz, Class<?>... parameterClasses) {
		if(this.getCache().privateConstructors().containsKey(clazz, parameterClasses))
			return this.getCache().privateConstructors().get(clazz, parameterClasses);
		
		try {
			SaveConstructor sc = new SaveConstructor<>(clazz.getDeclaredConstructor(parameterClasses));
			
			this.getCache().privateConstructors().put(clazz, parameterClasses, sc);
			
			return sc;
		} catch(NoSuchMethodException e) {
			return this.getExceptionHandler().noSuchPrivateConstructor(clazz, parameterClasses);
		}
	}
	
	public int getEnumConstantID(Object enumConstant) {
		return (int) this.getMethod("oridnal", Enum.class).invoke(enumConstant, false);
	}
	
	public SaveMethod getMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
		if(this.getCache().methods().containsKey(name, clazz, parameterClasses))
			return this.getCache().methods().get(name, clazz, parameterClasses);
		
		try {
			SaveMethod sm = new SaveMethod(clazz.getMethod(name, parameterClasses));
			
			this.getCache().methods().put(name, clazz, parameterClasses, sm);
			
			return sm;
		} catch(NoSuchMethodException e) {
			return this.getExceptionHandler().noSuchMethod(clazz, name, parameterClasses);
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getCache(), this.getExceptionHandler());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Reflections that = (Reflections) o;
		return Objects.equals(this.getCache(), that.getCache()) &&
				Objects.equals(this.getExceptionHandler(), that.getExceptionHandler());
	}
	
	@Override
	public String toString() {
		return "Reflections{" +
				"                            cache=" + this.cache +
				",                             exceptionHandler=" + this.exceptionHandler +
				'}';
	}
}
