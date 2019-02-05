package io.github.alphahelixdev.helius.file.yaml;

import lombok.EqualsAndHashCode;

import java.io.*;

@EqualsAndHashCode(callSuper = true)
public abstract class FileConfig extends ConfigSection {
	
	public FileConfig() {
		super(null, "", null);
	}
	
	public void save(String filePath) throws IOException {
		if(filePath == null || filePath.equals(""))
			return;
		
		this.save(new File(filePath));
	}
	
	public void save(File file) throws IOException {
		if(file == null)
			return;
		
		file.getParentFile().mkdirs();
		
		String data = saveToString();
		
		try(FileWriter writer = new FileWriter(file)) {
			writer.write(data);
		}
	}
	
	public abstract String saveToString();
	
	public void load(String file) throws IOException {
		if(file == null || file.equals(""))
			return;
		
		this.load(new File(file));
	}
	
	public void load(File file) throws IOException {
		this.load(new FileInputStream(file));
	}
	
	public void load(InputStream stream) throws IOException {
		if(stream == null)
			return;
		
		InputStreamReader reader = new InputStreamReader(stream);
		StringBuilder builder = new StringBuilder();
		
		try(BufferedReader input = new BufferedReader(reader)) {
			String line;
			
			while((line = input.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
		}
		
		this.loadFromString(builder.toString());
	}
	
	public abstract void loadFromString(String contents);
}
