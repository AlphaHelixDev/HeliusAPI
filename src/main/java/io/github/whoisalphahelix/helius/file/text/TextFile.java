package io.github.whoisalphahelix.helius.file.text;

import io.github.whoisalphahelix.helius.Helius;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
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
}
