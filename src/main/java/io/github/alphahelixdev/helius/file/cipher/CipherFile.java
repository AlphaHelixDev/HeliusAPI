package io.github.alphahelixdev.helius.file.cipher;

import io.github.alphahelixdev.helius.cipher.HeliusCipher;
import io.github.alphahelixdev.helius.file.text.TextFile;

import java.io.File;
import java.net.URI;

public class CipherFile extends TextFile {
	private final HeliusCipher cipher;
	
	public CipherFile(String pathname, int cipherKey) {
		super(pathname);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(String parent, String child, int cipherKey) {
		super(parent, child);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(File parent, String child, int cipherKey) {
		super(parent, child);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(URI uri, int cipherKey) {
		super(uri);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	@Override
	public TextFile write(String toWrite) {
		return super.write(this.cipher.encode(toWrite));
	}
	
	public String readCipher() {
		return this.cipher.decode(super.read());
	}
}
