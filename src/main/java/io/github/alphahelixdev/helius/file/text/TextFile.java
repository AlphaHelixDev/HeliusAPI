package io.github.alphahelixdev.helius.file.text;

import io.github.alphahelixdev.helius.Helius;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

public class TextFile extends File {
	
	private String content;
	
	public TextFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.content = read();
	}
	
	public String read() {
		try {
			return FileUtils.readFileToString(this, Charset.defaultCharset());
		} catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public TextFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.content = read();
	}
	
	public TextFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.content = read();
	}
	
	public TextFile(URI uri) {
		super(uri);
		Helius.createFile(this);
		this.content = read();
	}
	
	public TextFile write(String toWrite) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			writer.write(toWrite);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
