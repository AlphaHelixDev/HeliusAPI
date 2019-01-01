package io.github.alphahelixdev.helius.file.cipher;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.cipher.HeliusCipher;
import io.github.alphahelixdev.helius.file.text.TextFile;

import java.io.File;
import java.net.URI;
import java.util.Objects;

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
	
	public HeliusCipher getCipher() {
		return this.cipher;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		CipherFile that = (CipherFile) o;
		return Objects.equals(this.getCipher(), that.getCipher());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.getCipher());
	}
	
	@Override
	public String toString() {
		return "CipherFile{" +
				"                            cipher=" + this.cipher +
				'}';
	}
	
	public String readCipher() {
		return this.getCipher().decode(super.read());
	}
}
