package controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import dto.ErrorDto;
import play.Play;
import play.cache.Cache;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import play.mvc.Util;

import java.util.List;
import java.util.Map;

public class SearchController extends Controller {

    static final String URL = Play.configuration.getProperty("api.flight.url") + "/v1/getAutocomplete?q=";

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

        WSRequest request = WS.url(URL + q);

        JsonArray resultArray = request.get().getJson().getAsJsonArray();
        for (JsonElement elem : resultArray) {
            Map map = Maps.newHashMap();
            map.put("id", elem.getAsJsonObject().get("id").getAsString());
            map.put("name", elem.getAsJsonObject().get("name").getAsString());
            map.put("city", elem.getAsJsonObject().get("city").getAsString());
            result.add(map);
        }

        return result;
    }
    public static List getCachedAirports(String q){
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
    
    public static String getAirportName(String airportCode){
        List<Map> lista = getCachedAirports(airportCode);
        String airport = "";
        for (Map<String, String> o : lista) {
        	for (Map.Entry<String, String> entry : o.entrySet())
        	{
        		if (entry.getKey().equals("name")) {
        			airport = entry.getValue();
        			return airport;
        		}
        	}
		}
        return airport;
    }
    public static String getAirportCity(String airportCode){
        List<Map> lista = getCachedAirports(airportCode);
        String city = "";
        for (Map<String, String> o : lista) {
        	for (Map.Entry<String, String> entry : o.entrySet())
        	{
        		if (entry.getKey().equals("city") ) {
        			city = entry.getValue();
        			return city;
        		}
        	}
		}
        return city;
    }
}