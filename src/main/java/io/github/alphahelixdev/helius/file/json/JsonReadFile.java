package io.github.alphahelixdev.helius.file.json;

import com.google.gson.*;
import io.github.alphahelixdev.helius.Helius;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonReadFile extends File {
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonElement element;
	
	public JsonReadFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.setElement(read());
	}
	
	private JsonElement read() {
		String file = Helius.read(this);
		
		if(file.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		
		return this.getGson().fromJson(file, JsonElement.class);
	}
	
	public Gson getGson() {
		return this.gson;
	}
	
	public JsonReadFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setElement(read());
	}
	
	public JsonReadFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setElement(read());
	}
	
	public JsonReadFile(URI uri) {
		super(uri);
		Helius.createFile(this);
		this.setElement(read());
	}
	
	public List<JsonElement> getArrayValues() {
		List<JsonElement> objects = new ArrayList<>();
		
		if(!this.getElement().isJsonArray()) return objects;
		
		for(JsonElement element : this.getElement().getAsJsonArray()) {
			objects.add(element);
		}
		
		return objects;
	}
	
	public JsonElement getElement() {
		return this.element;
	}
	
	public JsonReadFile setElement(JsonElement element) {
		this.element = element;
		return this;
	}
	
	public JsonElement getValue(String key) {
		if(!this.getElement().isJsonObject()) return new JsonObject();
		
		return this.getElement().getAsJsonObject().get(key);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		JsonReadFile that = (JsonReadFile) o;
		return Objects.equals(this.getGson(), that.getGson()) &&
				Objects.equals(this.getElement(), that.getElement());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.getGson(), this.getElement());
	}
	
	@Override
	public String toString() {
		return "JsonReadFile{" +
				"                            gson=" + this.gson +
				",                             element=" + this.element +
				'}';
	}
}
