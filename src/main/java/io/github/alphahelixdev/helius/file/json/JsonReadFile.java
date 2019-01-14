package io.github.alphahelixdev.helius.file.json;

import com.google.gson.*;
import io.github.alphahelixdev.helius.Helius;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JsonReadFile extends File {
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonElement element;
	
	public JsonReadFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.setElement(read());
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
	
	private JsonElement read() {
		String file = Helius.read(this);
		
		if(file.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		
		return this.getGson().fromJson(file, JsonElement.class);
	}
	
	public List<JsonElement> getArrayValues() {
		List<JsonElement> objects = new ArrayList<>();
		
		if(!this.getElement().isJsonArray()) return objects;
		
		for(JsonElement element : this.getElement().getAsJsonArray()) {
			objects.add(element);
		}
		
		return objects;
	}
	
	public JsonElement getValue(String key) {
		if(!this.getElement().isJsonObject()) return new JsonObject();
		
		return this.getElement().getAsJsonObject().get(key);
	}
}
