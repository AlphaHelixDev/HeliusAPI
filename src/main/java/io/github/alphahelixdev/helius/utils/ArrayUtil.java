package io.github.alphahelixdev.helius.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayUtil {
	
	public static String[] replaceInArray(String pattern, String replace, String... array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = array[i].replace(pattern, replace);
		}
		return array;
	}
	
	public static <T> List<T> getTypesOf(Class<T> clazzType, Collection<Object> list) {
		return ArrayUtil.getTypesOf(clazzType, list.toArray());
	}
	
	public static <T> List<T> getTypesOf(Class<T> clazzType, Object... list) {
		List<T> types = new ArrayList<>();
		
		for(Object o : list)
			if(o.getClass().isInstance(clazzType))
				types.add((T) o);
		return types;
	}
	
	public static void checkForLength(int min, int max, Collection<Object> list) {
		ArrayUtil.checkForLength(min, max, list.toArray());
	}
	
	public static void checkForLength(int min, int max, Object... array) {
		if(array.length < min || array.length > max)
			throw new ArrayIndexOutOfBoundsException("You have to at least parse " + min + " arguments and up to " + max);
	}
	
	@SafeVarargs
	public static <T> T[] replaceInArray(T obj, int pos, T... array) {
		if(pos > array.length - 1)
			return array;
		
		array[pos] = obj;
		
		return array;
	}
	
	public static double min(double... a) {
		double lowest = a[0];
		
		for(double l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static int min(int... a) {
		int lowest = a[0];
		
		for(int l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static long min(long... a) {
		long lowest = a[0];
		
		for(long l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static float min(float... a) {
		float lowest = a[0];
		
		for(float l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static double max(double... a) {
		double highest = a[0];
		
		for(double l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static int max(int... a) {
		int highest = a[0];
		
		for(int l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static long max(long... a) {
		long highest = a[0];
		
		for(long l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static float max(float... a) {
		float highest = a[0];
		
		for(float l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static double sum(double... a) {
		double sum = a[0];
		
		for(double s : a)
			sum += s;
		
		return sum;
	}
	
	public static int sum(int... a) {
		int sum = a[0];
		
		for(int s : a)
			sum += s;
		
		return sum;
	}
	
	public static float sum(float... a) {
		float sum = a[0];
		
		for(float s : a)
			sum += s;
		
		return sum;
	}
	
	public static long sum(long... a) {
		long sum = a[0];
		
		for(long s : a)
			sum += s;
		
		return sum;
	}
	
	public static double[] trim(int decimal, double... a) {
		for(int i = 0; i < a.length; i++)
			a[i] = MathUtil.trim(a[i], decimal);
		
		return a;
	}
	
	public static double[][] trim(int decimal, double[]... coordinates) {
		for(int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new double[]{MathUtil.trim(coordinates[i][0], decimal), MathUtil.trim(coordinates[i][1], decimal),
					MathUtil.trim(coordinates[i][2], decimal)};
		}
		return coordinates;
	}
	
}
