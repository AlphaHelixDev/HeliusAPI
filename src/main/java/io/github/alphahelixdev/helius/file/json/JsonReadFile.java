package io.github.alphahelixdev.helius.file.json;

import com.google.gson.*;
import io.github.alphahelixdev.helius.Helius;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonReadFile extends File {
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonElement element;
	
	public JsonReadFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.element = read();
	}
	
	private JsonElement read() {
		try {
			String file = FileUtils.readFileToString(this, Charset.defaultCharset());
			
			if(file.isEmpty()) {
				return JsonNull.INSTANCE;
			}
			
			return gson.fromJson(file, JsonElement.class);
		} catch(IOException e) {
			e.printStackTrace();
			return JsonNull.INSTANCE;
		}
	}
	
	public JsonReadFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.element = read();
	}
	
	public JsonReadFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.element = read();
	}
	
	public JsonReadFile(URI uri) {
		super(uri);
		Helius.createFile(this);
		this.element = read();
	}
	
	public JsonElement getElement() {
		return element;
	}
	
	public List<JsonElement> getArrayValues() {
		List<JsonElement> objects = new ArrayList<>();
		
		if(!element.isJsonArray()) return objects;
		
		for(JsonElement element : this.element.getAsJsonArray()) {
			objects.add(element);
		}
		
		return objects;
	}
	
	public JsonElement getValue(String key) {
		if(!element.isJsonObject()) return new JsonObject();
		
		return element.getAsJsonObject().get(key);
	}
}
