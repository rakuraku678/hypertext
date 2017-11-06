package controllers;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dto.ErrorDto;
import play.Play;
import play.cache.Cache;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import play.mvc.Util;
import utils.JsonUtils;

public class SearchController extends Controller {

    static final String URL = Play.configuration.getProperty("api.flight.url");

    public static void getAutocomplete(String q) {
        try {
            List response = getCachedAutoComplete(q);

            Map result = Maps.newHashMap();
            result.put("items", response);

            renderJSON(result);
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
            ErrorDto errorDto = new ErrorDto(e.getMessage());
            renderJSON(errorDto);
        }
    }

    @Util
    public static List getCachedAutoComplete(String q) {
        final String key = "autocomplete-" + q;
        List r = Cache.get(key, List.class);
        if (r == null) {
            r = getAutoCompleteFromAPI(q);
            if (!r.isEmpty()) {
                Cache.set(key, r, "1d");
            }
        }
        return r;
    }

    private static List getAutoCompleteFromAPI(String q) {
        List result = Lists.newArrayList();

        WSRequest request = WS.url(URL + "/v1/getAutocomplete?q=" + q);

        JsonArray resultArray = request.get().getJson().getAsJsonArray();
        for (JsonElement elem : resultArray) {
            Map map = Maps.newHashMap();
            map.put("id", JsonUtils.getStringFromJson(elem.getAsJsonObject(),"id"));
            map.put("name", JsonUtils.getStringFromJson(elem.getAsJsonObject(),"name"));
            map.put("city", JsonUtils.getStringFromJson(elem.getAsJsonObject(),"city"));
            map.put("country", JsonUtils.getStringFromJson(elem.getAsJsonObject(),"country"));
            map.put("iataCityCode", JsonUtils.getStringFromJson(elem.getAsJsonObject(),"iataCityCode"));
            result.add(map);
        }

        return result;
    }
    
	public static Map getCity(String iatacode) {
		String url = Play.configuration.getProperty("flights.url");
		WSRequest request = WS.url(url + "/city/" + iatacode);
        

        JsonObject jsonObj = request.get().getJson().getAsJsonObject();
        Map map = Maps.newHashMap();
        map.put("id", JsonUtils.getStringFromJson(jsonObj,"id"));
        map.put("name", JsonUtils.getStringFromJson(jsonObj,"name"));
        map.put("city", JsonUtils.getStringFromJson(jsonObj,"city"));
        map.put("country", JsonUtils.getStringFromJson(jsonObj,"country"));
        map.put("iataCityCode", JsonUtils.getStringFromJson(jsonObj,"iataCityCode"));
        map.put("onlyPassport", JsonUtils.getBooleanFromJson(jsonObj,"onlyPassport"));

        return map;
    }
	
}