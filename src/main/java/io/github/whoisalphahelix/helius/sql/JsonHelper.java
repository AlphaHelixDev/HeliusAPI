/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 */
package io.github.whoisalphahelix.helius.sql;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonHelper {
	private static final JsonParser PARSER = new JsonParser();
	
	public static JsonElement toJsonTree(Object obj) {
		JsonObject head = new JsonObject();
		if(obj instanceof Number || obj instanceof String || obj instanceof Boolean || obj instanceof Character) {
			return SQLApi.gson().toJsonTree(obj);
		}
		head.add("body", SQLApi.gson().toJsonTree(obj));
		head.addProperty("type", obj.getClass().getName());
		return head;
	}
	
	public static Object fromJsonTree(String json) {
		if(!json.contains("body") || !json.contains("type")) {
			JsonPrimitive primitive = (JsonPrimitive) PARSER.parse(json);
			if(primitive.isBoolean()) {
				return primitive.getAsBoolean();
			}
			if(primitive.isNumber()) {
				return JsonHelper.findNumberType(primitive.getAsNumber());
			}
			if(primitive.isString()) {
				return primitive.getAsString();
			}
		}
		try {
			JsonObject obj = (JsonObject) PARSER.parse(json);
			return SQLApi.gson().fromJson(obj.get("body"), Class.forName(obj.get("type").getAsString()));
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Object findNumberType(Number num) {
		String a = num.toString();
		try {
			return Integer.parseInt(a);
		} catch(NumberFormatException numberFormatException) {
			try {
				return Double.parseDouble(a);
			} catch(NumberFormatException numberFormatException2) {
				try {
					return Float.parseFloat(a);
				} catch(NumberFormatException numberFormatException3) {
					try {
						return Long.parseLong(a);
					} catch(NumberFormatException numberFormatException4) {
						try {
							return Byte.parseByte(a);
						} catch(NumberFormatException numberFormatException5) {
							try {
								return Short.parseShort(a);
							} catch(NumberFormatException numberFormatException6) {
								return null;
							}
						}
					}
				}
			}
		}
	}
}

