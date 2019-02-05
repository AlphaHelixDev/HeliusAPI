package io.github.alphahelixdev.helius.file.cipher;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.cipher.HeliusCipher;
import io.github.alphahelixdev.helius.file.text.TextFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.net.URI;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class CipherFile extends TextFile {
	private final HeliusCipher cipher;
	
	public CipherFile(String pathname, int cipherKey) {
		super(pathname);
		Helius.createFile(this);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(String parent, String child, int cipherKey) {
		super(parent, child);
		Helius.createFile(this);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(File parent, String child, int cipherKey) {
		super(parent, child);
		Helius.createFile(this);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	public CipherFile(URI uri, int cipherKey) {
		super(uri);
		Helius.createFile(this);
		this.cipher = new HeliusCipher(cipherKey);
	}
	
	@Override
	public TextFile write(String toWrite) {
		return super.write(this.getCipher().encode(toWrite));
	}
	
	public String readCipher() {
		return this.getCipher().decode(super.read());
	}
}
