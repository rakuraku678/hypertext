package utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonElement;

import play.Play;
import play.cache.Cache;

public class CacheUtils {

    public static JsonElement getCachedFlightSelection(String key){
        
        JsonElement jsonSelection = Cache.get(key, JsonElement.class);
        if (jsonSelection == null) {
            return null;
        }
        return jsonSelection;
    }

	//TODO hay que hacer esto
	public static String generateKey(JsonElement bfmResultItem) {
		String key = DigestUtils.md5Hex(bfmResultItem.toString());
		System.out.println("la key guachin: "+key);
		return key;
	}
	public static void setSelectionFlight(JsonElement bfmResultItem) {
		String key = generateKey(bfmResultItem);
		Cache.set(key, bfmResultItem, "1d");
	}
}
