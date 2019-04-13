package io.github.whoisalphahelix.helius.file.json;

import com.google.gson.*;
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
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class JsonFile extends File {
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonObject head;
	private JsonArray arrayHead;
	
	public JsonFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
		this.setArrayHead(readArray());
		this.setHead((JsonObject) arrayHead.get(0));
	}
	
	public JsonFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setArrayHead(readArray());
		this.setHead((JsonObject) arrayHead.get(0));
	}
	
	public JsonFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
		this.setArrayHead(readArray());
		this.setHead((JsonObject) arrayHead.get(0));
	}
	
	public JsonFile(URI uri) {
		super(uri);
		Helius.createFile(this);
		this.setArrayHead(readArray());
		this.setHead((JsonObject) arrayHead.get(0));
	}
	
	private JsonArray readArray() {
		String file = Helius.read(this);
		JsonArray array = new JsonArray();
		
		if(file.isEmpty() || !(file.startsWith("[") || file.endsWith("]"))) {
			array.add(new JsonObject());
			
			return array;
		}
		
		array = this.getGson().fromJson(file, JsonArray.class);
		
		return array;
	}
	
	public JsonFile addArrayValue(Object value) {
		JsonObject obj = new JsonObject();
		
		obj.add("class", new JsonPrimitive(value.getClass().getCanonicalName()));
		obj.add("value", this.getGson().toJsonTree(value));
		
		this.getArrayHead().add(obj);
		return update();
	}
	
	public JsonFile update() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			this.getArrayHead().set(0, this.getHead());
			writer.write(this.getGson().toJson(this.getArrayHead()));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public JsonFile setDefaultValue(String path, Object value) {
		if(!contains(path))
			setValue(path, value);
		return this;
	}
	
	public boolean contains(String path) {
		Map.Entry<List<JsonObject>, List<String>> paths = getJsonPath(path);
		
		for(int i = 0; i < paths.getValue().size(); i++) {
			if(!paths.getKey().get(i).has(paths.getValue().get(i)))
				return false;
		}
		return true;
	}
	
	public <T> JsonFile edit(String path, Class<T> valueClass, Consumer<T> consumer) {
		T type = getValue(path, valueClass);
		
		consumer.accept(type);
		
		setValue(path, type);
		
		return this;
	}
	
	public JsonFile removeArrayValue(int index) {
		if(index == 0) return this;
		
		this.getArrayHead().remove(index);
		
		return update();
	}
	
	public boolean hasArrayValue(int index) {
		return this.getArrayHead().size() <= index;
	}
	
	public List<Object> getArrayValues() {
		List<Object> objs = new LinkedList<>();
		
		if(this.getArrayHead().size() == 1) return objs;
		
		for(int i = 1; i < this.getArrayHead().size(); i++) {
			JsonObject obj = (JsonObject) this.getArrayHead().get(i);
			
			try {
				Class<?> clss = Class.forName(obj.get("class").getAsString());
				objs.add(this.getGson().fromJson(obj.get("value"), clss));
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return objs;
	}
	
	public JsonFile setValue(String path, Object value) {
		Map.Entry<List<JsonObject>, List<String>> paths = getJsonPath(path);
		
		paths.getKey().get(paths.getKey().size() - 1).add(paths.getValue().get(paths.getValue().size() - 1),
				gson.toJsonTree(value));
		
		this.setHead(paths.getKey().get(0));
		
		update();
		return this;
	}
	
	private Map.Entry<List<JsonObject>, List<String>> getJsonPath(String path) {
		String[] pathArray = path.split("\\.");
		LinkedList<JsonObject> objects = new LinkedList<>();
		List<String> names = new LinkedList<>();
		
		objects.add(this.getHead());
		
		for(int i = 1; i < pathArray.length; i++) {
			if(objects.get(i - 1).has(pathArray[i - 1])) {
				objects.add(objects.get(i - 1).getAsJsonObject(pathArray[i - 1]));
			} else {
				objects.add(new JsonObject());
			}
		}
		
		for(int i = 0; i < objects.size() - 1; i++) {
			objects.get(i).add(pathArray[i], objects.get(i + 1));
			names.add(pathArray[i]);
		}
		
		names.add(pathArray[pathArray.length - 1]);
		
		return new AbstractMap.SimpleEntry<>(objects, names);
	}
	
	public <T> T getValue(String path, Class<T> valueClass) {
		if(!contains(path)) return null;
		
		Map.Entry<List<JsonObject>, List<String>> paths = getJsonPath(path);
		
		return this.getGson().fromJson(paths.getKey().get(paths.getKey().size() - 1)
				.get(paths.getValue().get(paths.getValue().size() - 1)), valueClass);
	}
}
