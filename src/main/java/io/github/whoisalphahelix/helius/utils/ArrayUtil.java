package io.github.whoisalphahelix.helius.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		return (List<T>) Arrays.stream(list).filter(o -> o.getClass().isInstance(clazzType))
		                       .collect(Collectors.toList());
	}
	
	public static void checkForLength(int min, int max, Collection<Object> list) {
		ArrayUtil.checkForLength(min, max, list.toArray());
	}
	
	public static void checkForLength(int min, int max, Object... array) {
		if(array.length < min || array.length > max)
			throw new ArrayIndexOutOfBoundsException("You have to at least parse " + min + " arguments and up to "
					                                         + max);
	}
	
	@SafeVarargs
	public static <T> T[] replaceInArray(T obj, int pos, T... array) {
		if(pos > array.length - 1)
			return array;
		
		array[pos] = obj;
		
		return array;
	}
	
	public static double min(double... a) {
		return Arrays.stream(a).min().orElse(a[0]);
	}
	
	public static int min(int... a) {
		return Arrays.stream(a).min().orElse(a[0]);
	}
	
	public static long min(long... a) {
		return Arrays.stream(a).min().orElse(a[0]);
	}
	
	public static double max(double... a) {
		return Arrays.stream(a).max().orElse(a[0]);
	}
	
	public static int max(int... a) {
		return Arrays.stream(a).max().orElse(a[0]);
	}
	
	public static long max(long... a) {
		return Arrays.stream(a).max().orElse(a[0]);
	}
	
	public static double sum(double... a) {
		return Arrays.stream(a).sum();
	}
	
	public static int sum(int... a) {
		return Arrays.stream(a).sum();
	}

	public static long sum(long... a) {
		return Arrays.stream(a).sum();
	}
	
	public static double[] trim(int decimal, double... a) {
		for(int i = 0; i < a.length; i++)
			a[i] = MathUtil.trim(a[i], decimal);
		
		return a;
	}
	
	public static double[][] trim(int decimal, double[]... coordinates) {
		for(int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new double[]{MathUtil.trim(coordinates[i][0], decimal), MathUtil.trim(coordinates[i][1],
			                                                                                       decimal),
					MathUtil.trim(coordinates[i][2], decimal)};
		}
		return coordinates;
	}

	public static <T> List<T> merge(List<List<T>> list) {
		return list.stream().reduce((l1, l2) -> {
			l1.addAll(l2);
			return l1;
		}).orElse(new ArrayList<>());
	}
	
	public static <A, B> B[] map(A[] arr, Function<A, B> mapper) {
		return (B[]) Arrays.stream(arr).map(mapper).toArray();
	}
}
