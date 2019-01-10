package io.github.alphahelixdev.helius.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

	public static List<String> upperEverything(Collection<String> collection) {
		return collection.stream().map(String::toUpperCase).collect(Collectors.toList());
	}

	public static List<String> lowerEverything(Collection<String> collection) {
		return collection.stream().map(String::toLowerCase).collect(Collectors.toList());
	}
	
	public static boolean matches(String str, String... possible) {
		return Arrays.asList(possible).contains(str);
	}
}
