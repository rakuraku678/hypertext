package utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonElement;

import play.Play;
import play.cache.Cache;

public class CacheUtils {

    public static String getCachedFlightSelection(String key){
        
        String selectedFlightBfm = Cache.get(key, String.class);
        if (selectedFlightBfm == null) {
            return null;
        }
        return selectedFlightBfm;
    }

	//TODO hay que hacer esto
	public static String generateKey(JsonElement bfmResultItem) {
		String key = bfmResultItem.getAsJsonObject().get("transactionId").getAsString();
		System.out.println("la key: "+key);
		return key;
	}
	public static void setSelectionFlight(JsonElement bfmResultItem) {
		String key = generateKey(bfmResultItem);
		Cache.set(key, bfmResultItem.toString(), "1d");
	}
}
