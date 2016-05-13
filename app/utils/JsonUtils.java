package utils;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class JsonUtils {


	public static String getStringFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsString();
	}

	public static Double getDoubleFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsDouble();
	}

	public static Integer getIntegerFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsInt();
	}

	public static Boolean getBooleanFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsBoolean();
	}

	public static List<String> getStringListFromJson(JsonObject json, String fieldName) {
		List<String> resultList = Lists.newArrayList();

		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}

		JsonArray jsonArray = fieldJsonElement.getAsJsonArray();
		for (JsonElement jsonElement : jsonArray) {
			String str = jsonElement.getAsString();
			resultList.add(str);
		}
		return resultList;
	}

	public static List<Long> getLongListFromJson(JsonObject json, String fieldName) {
		List<Long> resultList = Lists.newArrayList();

		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}

		JsonArray jsonArray = fieldJsonElement.getAsJsonArray();
		for (JsonElement jsonElement : jsonArray) {
			long lng = jsonElement.getAsLong();
			resultList.add(lng);
		}
		return resultList;
	}

	public static JsonArray getJsonArrayFromJson(JsonObject json, String fieldName) {

		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsJsonArray();
	}

	public static Long getLongFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsLong();
	}

	public static JsonElement getJsonObjectFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return fieldJsonElement.getAsJsonObject();
	}

	public static Date getDateFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return new DateTime(fieldJsonElement.getAsString()).toDate();
	}

	public static DateTime getDateTimeFromJson(JsonObject json, String fieldName) {
		JsonElement fieldJsonElement = json.get(fieldName);
		if (fieldJsonElement == null || fieldJsonElement.isJsonNull()) {
			return null;
		}
		return new DateTime(fieldJsonElement.getAsString());
	}

}
