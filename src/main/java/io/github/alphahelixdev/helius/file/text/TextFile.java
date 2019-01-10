package io.github.alphahelixdev.helius.file.text;

import io.github.alphahelixdev.helius.Helius;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class TextFile extends File {
	
	private String content;
	
	public TextFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.setContent(read());
	}
	
	public String read() {
		return Helius.read(this);
	}
	
	public TextFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setContent(read());
	}
	
	public TextFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setContent(read());
	}
	
	public TextFile(URI uri) {
		super(uri);
		Helius.createFile(this);
		this.setContent(read());
	}
	
	public TextFile write(String toWrite) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			this.setContent(this.getContent() + System.lineSeparator() + toWrite);
			writer.write(this.getContent());
		} catch(IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public TextFile setContent(String content) {
		this.content = content;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		TextFile textFile = (TextFile) o;
		return Objects.equals(this.getContent(), textFile.getContent());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.getContent());
	}
	
	@Override
	public String toString() {
		return "TextFile{" +
				"content='" + this.content + '\'' +
				'}';
	}
}
