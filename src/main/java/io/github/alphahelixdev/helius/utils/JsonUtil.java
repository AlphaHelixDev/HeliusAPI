package io.github.alphahelixdev.helius.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JsonUtil {

	public static String toMappedJson(Gson gson, String path, Object obj, Consumer<JsonObject> lastAdd) {
		List<JsonObject> jsonObjects = new ArrayList<>();
		String[] sections = path.split("\\.");

		for(int i = 0; i < sections.length; i++)
			jsonObjects.add(i, new JsonObject());

		for(int i = 0; i < sections.length; i++) {
			JsonObject current = jsonObjects.get(i);
			JsonObject top = getBefore(jsonObjects, i);

			if(i != 0)
				top.add(sections[i - 1], current);

			if(i == sections.length - 1) {
				current.add(sections[i], gson.toJsonTree(obj));
				lastAdd.accept(current);
			}
		}

		return jsonObjects.get(0).toString();
	}

	private static JsonObject getBefore(List<JsonObject> objects, int id) {
		return objects.get(id == 0 ? 0 : id - 1);
	}
}
