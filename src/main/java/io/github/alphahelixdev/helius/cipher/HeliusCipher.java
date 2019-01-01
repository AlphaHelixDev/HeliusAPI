package io.github.alphahelixdev.helius.cipher;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

public class HeliusCipher {
	
	private static final Map<Character, Short> ALPHABETICAL_NUMBERS = ImmutableMap.<Character, Short>builder()
			.put('A', (short) 1).put('B', (short) 2).put('C', (short) 3)
			.put('D', (short) 4).put('E', (short) 5).put('F', (short) 6)
			.put('G', (short) 7).put('H', (short) 8).put('I', (short) 9)
			.put('J', (short) 10).put('K', (short) 11).put('L', (short) 12)
			.put('M', (short) 13).put('N', (short) 14).put('O', (short) 15)
			.put('P', (short) 16).put('Q', (short) 17).put('R', (short) 18)
			.put('S', (short) 19).put('T', (short) 20).put('U', (short) 21)
			.put('V', (short) 22).put('W', (short) 23).put('X', (short) 24)
			.put('Y', (short) 25).put('Z', (short) 26).put('a', (short) 27)
			.put('b', (short) 28).put('c', (short) 29).put('d', (short) 30)
			.put('e', (short) 31).put('f', (short) 32).put('g', (short) 33)
			.put('h', (short) 34).put('i', (short) 35).put('j', (short) 36)
			.put('k', (short) 37).put('l', (short) 38).put('m', (short) 39)
			.put('n', (short) 40).put('o', (short) 41).put('p', (short) 42)
			.put('q', (short) 43).put('r', (short) 44).put('s', (short) 45)
			.put('t', (short) 46).put('u', (short) 47).put('v', (short) 48)
			.put('w', (short) 49).put('x', (short) 50).put('y', (short) 51)
			.put('z', (short) 52).put(' ', (short) 53).put('1', (short) 54)
			.put('2', (short) 55).put('3', (short) 56).put('4', (short) 57)
			.put('5', (short) 58).put('6', (short) 59).put('7', (short) 60)
			.put('8', (short) 61).put('9', (short) 62).put('0', (short) 63)
			.put('.', (short) 64).put('?', (short) 65).put('!', (short) 66)
			.put(',', (short) 67).put('+', (short) 68).put('-', (short) 69)
			.put('*', (short) 70).put('/', (short) 71).put('=', (short) 72)
			.put('\\', (short) 73).put('\'', (short) 74).put('”', (short) 75)
			.put('§', (short) 76).put('%', (short) 77).put('&', (short) 78)
			.put('(', (short) 79).put(')', (short) 80).put('`', (short) 81)
			.put('´', (short) 82).put('^', (short) 83).put('°', (short) 84)
			.put('_', (short) 85).put(':', (short) 86).put(';', (short) 87)
			.put('@', (short) 88).put('€', (short) 89).put('#', (short) 90)
			.put('¬', (short) 91).put('£', (short) 93).put('ﬁ', (short) 94)
			.put('˜', (short) 95).put('·', (short) 96).put('¯', (short) 97)
			.put('', (short) 98).put('÷', (short) 99).put('˛', (short) 100)
			.put('˘', (short) 101).put('›', (short) 102).put('‹', (short) 103)
			.put('◊', (short) 104).put('Ç', (short) 105).put('Ù', (short) 106)
			.put('‡', (short) 107).put('>', (short) 108).put('<', (short) 109)
			.put('≥', (short) 110).put('≤', (short) 111).put('Å', (short) 112)
			.put('Í', (short) 113).put('™', (short) 114).put('Ì', (short) 115)
			.put('Ó', (short) 116).put('ı', (short) 117).put('ˆ', (short) 118)
			.put('ﬂ', (short) 119).put('Ö', (short) 120).put('Œ', (short) 121)
			.put('Ä', (short) 122).put('Æ', (short) 123).put('»', (short) 124)
			.put('„', (short) 125).put('‰', (short) 126).put('¸', (short) 127)
			.put('˝', (short) 128).put('ˇ', (short) 129).put('Á', (short) 130)
			.put('Û', (short) 131).put('Ø', (short) 132).put('∏', (short) 133)
			.put('’', (short) 134).put('«', (short) 135).put('∑', (short) 136)
			.put('®', (short) 137).put('†', (short) 138).put('Ω', (short) 139)
			.put('¨', (short) 140).put('⁄', (short) 141).put('ø', (short) 142)
			.put('π', (short) 143).put('•', (short) 144).put('±', (short) 145)
			.put('‘', (short) 146).put('—', (short) 147).put('–', (short) 148)
			.put('…', (short) 149).put('∞', (short) 150).put('µ', (short) 151)
			.put('~', (short) 152).put('∫', (short) 153).put('√', (short) 154)
			.put('ç', (short) 155).put('≈', (short) 156).put('¥', (short) 157)
			.put('å', (short) 158).put('‚', (short) 159).put('∂', (short) 160)
			.put('ƒ', (short) 161).put('©', (short) 162).put('ª', (short) 163)
			.put('º', (short) 164).put('∆', (short) 165).put('œ', (short) 166)
			.put('æ', (short) 167).put('Ü', (short) 168).put('ü', (short) 169)
			.put('ä', (short) 170).put('ö', (short) 171).put('¡', (short) 172)
			.put('“', (short) 173).put('¶', (short) 174).put('¢', (short) 175)
			.put('[', (short) 176).put(']', (short) 177).put('|', (short) 178)
			.put('{', (short) 179).put('}', (short) 180).put('≠', (short) 181)
			.put('¿', (short) 182).put('˙', (short) 183).build();
	
	private static final long CIPHER_NUMBER = 8539734;
	private static final HeliusCipher UNSAFE_CIPHER = new HeliusCipher(1);
	private static final byte SUB_SIZE = 17;
	private final long privateCipherKey;
	
	public HeliusCipher() {
		this(1);
	}
	
	public HeliusCipher(long key) {
		privateCipherKey = key * HeliusCipher.getCipherNumber();
	}
	
	public static long getCipherNumber() {
		return HeliusCipher.CIPHER_NUMBER;
	}
	
	public static String decode(String encoded, int key) {
		StringBuilder normal = new StringBuilder();
		String value = encoded.replace("HCE{", "").replace("}", "");
		
		for(int s = 0; s < value.length(); s += HeliusCipher.getSubSize()) {
			String sub = value.substring(s, s + HeliusCipher.getSubSize());
			
			long par = Long.parseLong(sub) - (key * HeliusCipher.getCipherNumber());
			short code = (short) (Math.sqrt(par));
			
			normal.append(getByShort(code));
		}
		return normal.toString();
	}
	
	public static byte getSubSize() {
		return HeliusCipher.SUB_SIZE;
	}
	
	private static char getByShort(short code) {
		for(Map.Entry<Character, Short> e : HeliusCipher.getAlphabeticalNumbers().entrySet()) {
			if(e.getValue() == code) return e.getKey();
		}
		return '�';
	}
	
	public static Map<Character, Short> getAlphabeticalNumbers() {
		return HeliusCipher.ALPHABETICAL_NUMBERS;
	}
	
	public static HeliusCipher getUnsafeCipher() {
		return UNSAFE_CIPHER;
	}
	
	public String encode(String normal) {
		StringBuilder encoder = new StringBuilder();
		for(char c : normal.toCharArray()) {
			short b = HeliusCipher.getAlphabeticalNumbers().getOrDefault(c, (short) 184);
			String value = "" + ((b * b) + this.getPrivateCipherKey());
			
			if(value.length() < HeliusCipher.getSubSize())
				value = upToEight(value);
			encoder.append(value);
		}
		
		return encoder.insert(0, "HCE{").append("}").toString();
	}
	
	public long getPrivateCipherKey() {
		return this.privateCipherKey;
	}
	
	private String upToEight(String full) {
		StringBuilder ne = new StringBuilder(full);
		for(int i = 0; i < HeliusCipher.getSubSize() && ne.length() < HeliusCipher.getSubSize(); i++) {
			ne.insert(0, "0");
		}
		return ne.toString();
	}
	
	public String decode(String encoded) {
		StringBuilder normal = new StringBuilder();
		String value = encoded.replace("HCE{", "").replace("}", "");
		
		for(int s = 0; s < value.length(); s += HeliusCipher.getSubSize()) {
			String sub = value.substring(s, s + HeliusCipher.getSubSize());
			
			long par = Long.parseLong(sub) - this.getPrivateCipherKey();
			
			short code = (short) (Math.sqrt(par));
			
			normal.append(getByShort(code));
		}
		return normal.toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getPrivateCipherKey());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		HeliusCipher cipher = (HeliusCipher) o;
		return this.getPrivateCipherKey() == cipher.getPrivateCipherKey();
	}
	
	@Override
	public String toString() {
		return "HeliusCipher{" +
				"                            privateCipherKey=" + this.privateCipherKey +
				'}';
	}
}
