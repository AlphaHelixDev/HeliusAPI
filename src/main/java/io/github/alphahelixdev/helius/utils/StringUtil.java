package io.github.alphahelixdev.helius.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {
	
	private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public static String generateRandomString(int size) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i++) {
			char c = StringUtil.getAlphabet()[ThreadLocalRandom.current().nextInt(StringUtil.getAlphabet().length)];
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static char[] getAlphabet() {
		return StringUtil.ALPHABET;
	}
	
	public static boolean isLong(String s) {
		Scanner sc = new Scanner(s.trim());
		
		if(!sc.hasNextLong()) return false;
		
		sc.nextLong();
		return !sc.hasNext();
	}
	
	public static boolean isDouble(String s) {
		Scanner sc = new Scanner(s.trim());
		
		if(!sc.hasNextDouble()) return false;
		
		sc.nextDouble();
		return !sc.hasNext();
	}
	
	public static String replaceLast(String string, String toReplace, String replacement) {
		int pos = string.lastIndexOf(toReplace);
		if(pos > -1) {
			return string.substring(0, pos)
					+ replacement
					+ string.substring(pos + toReplace.length());
		} else {
			return string;
		}
	}
	
	public static Collection<String> upperEverything(Collection<String> collection) {
		String[] strings = collection.toArray(new String[collection.size()]);
		collection.clear();
		
		for(String str : strings)
			collection.add(str.toLowerCase());
		return collection;
	}
	
	public static Collection<String> lowerEverything(Collection<String> collection) {
		String[] strings = collection.toArray(new String[collection.size()]);
		collection.clear();
		
		for(String str : strings)
			collection.add(str.toLowerCase());
		return collection;
	}
	
	public static boolean matches(String str, String... possible) {
		return Arrays.asList(possible).contains(str);
	}
}
