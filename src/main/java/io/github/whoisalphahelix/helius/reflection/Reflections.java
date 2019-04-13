package io.github.whoisalphahelix.helius.reflection;


import io.github.whoisalphahelix.helius.Helius;
import io.github.whoisalphahelix.helius.reflection.exceptions.ExceptionHandler;
import io.github.whoisalphahelix.helius.reflection.exceptions.NullExceptionHandler;
import io.github.whoisalphahelix.helius.utils.ArrayUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Reflections {
	
	private final ReflectiveCache cache = new ReflectiveCache();
	private ExceptionHandler exceptionHandler;
	
	public Reflections() {
		this.setExceptionHandler(new NullExceptionHandler());
	}
	
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
	
	public List<SaveField> getFields(Class<?>... classes) {
		return ArrayUtil.merge(Arrays.stream(classes).map(this::getFields).collect(Collectors.toList()));
	}
	
	public List<SaveField> getDeclaredFields(Class<?>... classes) {
		return ArrayUtil.merge(Arrays.stream(classes).map(this::getDeclaredFields).collect(Collectors.toList()));
	}
	
	public List<SaveField> getFields(Class<?> clazz, boolean supers) {
		if(supers)
			return getFields(getSuperClasses(clazz).toArray(new Class[0]));
		return getFields(clazz);
	}
	
	public List<SaveField> getDeclaredFields(Class<?> clazz, boolean supers) {
		if(supers)
			return getDeclaredFields(getSuperClasses(clazz).toArray(new Class[0]));
		return getDeclaredFields(clazz);
	}
	
	public List<SaveField> getFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getFields(clazz).stream().filter(saveField -> saveField.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
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
		return this.getDeclaredFields(clazz).stream().filter(saveField -> saveField.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
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
		return this.getFields(clazz).stream().filter(saveField -> !saveField.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	public List<SaveField> getDeclaredFieldsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getDeclaredFields(clazz).stream().filter(saveField -> !saveField.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}
	
	public SaveField getFirstFieldWithType(Class<?> type, Class<?> clazz) {
		return this.getFieldsWithType(type, clazz).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchField(clazz, type));
	}
	
	public List<SaveField> getFieldsWithType(Class<?> type, Class<?> clazz) {
		return this.getFields(clazz).stream().filter(saveField -> saveField.asNormal().getType().equals(type))
				.collect(Collectors.toList());
	}
	
	public SaveField getLastFieldWithType(Class<?> type, Class<?> clazz) {
		return this.getFieldsWithType(type, clazz).stream().reduce((saveField, saveField2) -> saveField2)
				.orElse(this.getExceptionHandler().noSuchField(clazz, type));
	}
	
	public SaveField getFirstDeclaredFieldWithType(Class<?> type, Class<?> clazz) {
		return this.getDeclaredFieldsWithType(type, clazz).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchPrivateField(clazz, type));
	}
	
	public List<SaveField> getDeclaredFieldsWithType(Class<?> type, Class<?> clazz) {
		return this.getDeclaredFields(clazz).stream().filter(saveField -> saveField.asNormal().getType().equals(type))
				.collect(Collectors.toList());
	}
	
	public SaveField getLastDeclaredFieldWithType(Class<?> type, Class<?> clazz) {
		return this.getDeclaredFieldsWithType(type, clazz).stream()
				.reduce((saveField, saveField2) -> saveField2)
				.orElse(this.getExceptionHandler().noSuchField(clazz, type));
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
	
	public List<SaveMethod> getMethodsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}
	
	public List<SaveMethod> getMethods(Class<?> clazz) {
		return Arrays.stream(clazz.getMethods()).map(SaveMethod::new).collect(Collectors.toList());
	}
	
	public List<SaveMethod> getDeclaredMethodsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getDeclaredMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}
	
	public List<SaveMethod> getDeclaredMethods(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredMethods()).map(SaveMethod::new).collect(Collectors.toList());
	}
	
	public List<SaveMethod> getMethodsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getMethods(clazz).stream().filter(saveMethod -> !saveMethod.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}
	
	public List<SaveMethod> getDeclaredMethodsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getDeclaredMethods(clazz).stream().filter(saveMethod -> !saveMethod.asNormal()
				.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}
	
	public SaveMethod getFirstMethodReturning(Class<?> type, Class<?> clazz) {
		return this.getMethodsReturning(type, clazz).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, type));
	}
	
	public List<SaveMethod> getMethodsReturning(Class<?> type, Class<?> clazz) {
		return this.getMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal().getReturnType().equals(type))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastMethodReturning(Class<?> type, Class<?> clazz) {
		return this.getMethodsReturning(type, clazz).stream().reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, type));
	}
	
	public SaveMethod getFirstDeclaredMethodReturning(Class<?> type, Class<?> clazz) {
		return this.getDeclaredMethodsReturning(type, clazz).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, type));
	}
	
	public List<SaveMethod> getDeclaredMethodsReturning(Class<?> type, Class<?> clazz) {
		return this.getDeclaredMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal().getReturnType().equals(type))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastDeclaredMethodReturning(Class<?> type, Class<?> clazz) {
		return this.getDeclaredMethodsReturning(type, clazz).stream().reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, type));
	}
	
	public SaveMethod getFistMethodWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getMethodsWithParameters(clazz, parameterTypes).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, parameterTypes));
	}
	
	public List<SaveMethod> getMethodsWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getMethods(clazz).stream()
				.filter(saveMethod -> Arrays.equals(saveMethod.asNormal().getParameterTypes(), parameterTypes))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastMethodWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getMethodsWithParameters(clazz, parameterTypes).stream()
				.reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, parameterTypes));
	}
	
	public SaveMethod getFistDeclaredMethodWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getDeclaredMethodsWithParameters(clazz, parameterTypes).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, parameterTypes));
	}
	
	public List<SaveMethod> getDeclaredMethodsWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getDeclaredMethods(clazz).stream()
				.filter(saveMethod -> Arrays.equals(saveMethod.asNormal().getParameterTypes(), parameterTypes))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastDeclaredMethodWithParameters(Class<?> clazz, Class<?>... parameterTypes) {
		return this.getDeclaredMethodsWithParameters(clazz, parameterTypes).stream()
				.reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, parameterTypes));
	}
	
	public SaveMethod getFirstMethodWithName(Class<?> clazz, String name) {
		return this.getMethodsWithName(clazz, name).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, name));
	}
	
	public List<SaveMethod> getMethodsWithName(Class<?> clazz, String name) {
		return this.getMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal().getName().equals(name))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastMethodWithName(Class<?> clazz, String name) {
		return this.getMethodsWithName(clazz, name).stream().reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchMethod(clazz, name));
	}
	
	public SaveMethod getFirstDeclaredMethodWithName(Class<?> clazz, String name) {
		return this.getDeclaredMethodsWithName(clazz, name).stream().findFirst()
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, name));
	}
	
	public List<SaveMethod> getDeclaredMethodsWithName(Class<?> clazz, String name) {
		return this.getDeclaredMethods(clazz).stream().filter(saveMethod -> saveMethod.asNormal().getName().equals(name))
				.collect(Collectors.toList());
	}
	
	public SaveMethod getLastDeclaredMethodWithName(Class<?> clazz, String name) {
		return this.getDeclaredMethodsWithName(clazz, name).stream().reduce((saveMethod, saveMethod2) -> saveMethod2)
				.orElse(this.getExceptionHandler().noSuchPrivateMethod(clazz, name));
	}
	
	public Set<Class<?>> findClassesAnnotated(Class<? extends Annotation> annotation, Class<?>... classes) {
		return Arrays.stream(classes).filter(aClass -> aClass.isAnnotationPresent(annotation)
				&& !annotation.equals(aClass)).collect(Collectors.toSet());
	}
	
	public Class<?>[] findClassesImplementing(Class<?> interfaze, Class<?>... classes) {
		return Arrays.stream(classes).filter(aClass -> interfaze.isAssignableFrom(aClass) && !interfaze.equals(aClass))
				.toArray(Class[]::new);
	}
	
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
	
	public Set<Class<?>[]> getClassesFromFolder(File folder) {
		File[] jars = folder.listFiles();
		
		if(jars == null)
			return new HashSet<>();
		
		return Arrays.stream(jars).filter(file -> file.getName().endsWith(".jar"))
				.map(this::getClassesFromJar).collect(Collectors.toSet());
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
	
	public List<Class<?>> getSuperClasses(Class<?> baseClass) {
		List<Class<?>> supers = new ArrayList<>(Collections.singletonList(baseClass));
		
		while(baseClass.getSuperclass() != null) {
			supers.add(baseClass.getSuperclass());
			baseClass = baseClass.getSuperclass();
		}
		
		return supers;
	}
	
	public SaveConstructor getConstructor(Class<?> clazz, Class<?>... parameterClasses) {
		if(this.getCache().constructors().containsKey(clazz, parameterClasses))
			return this.getCache().constructors().get(clazz, parameterClasses);
		
		try {
			SaveConstructor sc = new SaveConstructor(clazz.getConstructor(parameterClasses));
			
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
			SaveConstructor sc = new SaveConstructor(clazz.getDeclaredConstructor(parameterClasses));
			
			this.getCache().privateConstructors().put(clazz, parameterClasses, sc);
			
			return sc;
		} catch(NoSuchMethodException e) {
			return this.getExceptionHandler().noSuchPrivateConstructor(clazz, parameterClasses);
		}
	}
	
	public List<SaveConstructor> getConstructorsAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getConstructors(clazz).stream()
				.filter(saveConstructor -> saveConstructor.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	public List<SaveConstructor> getConstructors(Class<?> clazz) {
		return Arrays.stream(clazz.getConstructors()).map(SaveConstructor::new).collect(Collectors.toList());
	}
	
	public List<SaveConstructor> getDeclaredConstructorsAnnotated(Class<?> clazz,
	                                                              Class<? extends Annotation> annotation) {
		return this.getDeclaredConstructors(clazz).stream()
				.filter(saveConstructor -> saveConstructor.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	public int getEnumConstantID(Object enumConstant) {
		return (int) this.getMethod("oridnal", Enum.class).invoke(enumConstant, false);
	}
	
	public List<SaveConstructor> getDeclaredConstructors(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredConstructors()).map(SaveConstructor::new).collect(Collectors.toList());
	}
	
	public List<SaveConstructor> getConstructorsNotAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
		return this.getConstructors(clazz).stream()
				.filter(saveConstructor -> !saveConstructor.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	public List<SaveConstructor> getDeclaredConstructorsNotAnnotated(Class<?> clazz,
	                                                                 Class<? extends Annotation> annotation) {
		return this.getDeclaredConstructors(clazz).stream()
				.filter(saveConstructor -> !saveConstructor.asNormal().isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
}
