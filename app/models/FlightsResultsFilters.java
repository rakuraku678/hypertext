package models;

import java.util.List;
import java.util.Map;

import utils.ApiFlightsSdk.v1.AirlinesSearch;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controllers.SearchController;

public class FlightsResultsFilters {
    public Map<String,Integer> outbounflightstops = Maps.newHashMap();
    public Map<String,Integer> inbounflightstops = Maps.newHashMap();
    public Map<String,Integer> carriers = Maps.newHashMap();
    public Map<String,Integer> outboundAirport = Maps.newHashMap();
    public Map<String,Integer> inboundAirport = Maps.newHashMap();
    public Map<String,Integer> carriersNames = Maps.newHashMap();
    public Map<String,String> carriersCodesXNames = Maps.newHashMap();
    public Map<String,String> outboundAirportCodes = Maps.newHashMap();
    public Map<String,String> inboundAirportCodes = Maps.newHashMap();

    public static FlightsResultsFilters processFlightsResults(JsonElement flightsResults){
        FlightsResultsFilters flightsResultsFilters = new FlightsResultsFilters();

        for (JsonElement jsonElement : flightsResults.getAsJsonArray()) {
            JsonObject flightsResult = jsonElement.getAsJsonObject();
            JsonObject departureSegment = flightsResult.getAsJsonObject("departureSegment");
            JsonObject returnSegment = flightsResult.getAsJsonObject("returnSegment");
            JsonArray departureSegmentDetail = departureSegment.getAsJsonArray("detail");
            JsonArray returnSegmentDetail = returnSegment.getAsJsonArray("detail");

            flightsResultsFilters.addOutbounFlightstops(departureSegmentDetail.size());
            flightsResultsFilters.addInbounFlightstops(returnSegmentDetail.size());

            for(JsonElement departureSegmentsElement : departureSegmentDetail){
                JsonObject departureSegmentsObject = departureSegmentsElement.getAsJsonObject();
                flightsResultsFilters.addCarriers(departureSegmentsObject.get("operatingAirlineCode").getAsString());
            }
            for(JsonElement returnSegmentsElement : returnSegmentDetail){
                JsonObject returnSegmentsObject = returnSegmentsElement.getAsJsonObject();
                flightsResultsFilters.addCarriers(returnSegmentsObject.get("operatingAirlineCode").getAsString());
            }
            
            
            JsonObject firstDepartureSegment = departureSegmentDetail.get(0).getAsJsonObject();
            JsonObject firstReturnSegment = returnSegmentDetail.get(0).getAsJsonObject();

            
            String departureAirportCode = firstDepartureSegment.get("departureAirportCode").getAsString();
            List<Map> lista = SearchController.getCachedAirports(departureAirportCode);

            for (Map<String, String> o : lista) {
            	for (Map.Entry<String, String> entry : o.entrySet())
            	{
            		if (entry.getKey().equals("name")) {
            			flightsResultsFilters.addOutboundAirport(entry.getValue());
            			System.out.println("poniendo airport code: "+departureAirportCode);
            			flightsResultsFilters.outboundAirportCodes.put(entry.getValue(),departureAirportCode);
            		}
            	}
    		}
            
            String returnAirportCode = firstReturnSegment.get("departureAirportCode").getAsString();
            lista = SearchController.getCachedAirports(returnAirportCode);
            
            for (Map<String, String> o : lista) {
            	for (Map.Entry<String, String> entry : o.entrySet())
            	{
            		if (entry.getKey().equals("name")) {
            			flightsResultsFilters.addInboundAirport(entry.getValue());
            			flightsResultsFilters.inboundAirportCodes.put(entry.getValue(), returnAirportCode);
            		}
            	}
    		}
        }

        StringBuilder airlineCodes = new StringBuilder();
        for (Map.Entry<String, Integer> entry : flightsResultsFilters.carriers.entrySet())
        {
        	airlineCodes.append(entry.getKey()+",");
        }
        
        JsonElement json = AirlinesSearch.process(airlineCodes.toString());
        
        if (!json.isJsonNull()){
        	flightsResultsFilters.carriersCodesXNames = Maps.newHashMap();
        	JsonArray jArray = json.getAsJsonArray();
        	for (int i = 0; i < jArray.size(); i++) {
        		String code = jArray.get(i).getAsJsonObject().get("iataCode").getAsString();
        		String name = jArray.get(i).getAsJsonObject().get("name").getAsString();
        		flightsResultsFilters.carriersCodesXNames.put(code, name);
        		
			}
        }
        
        
        return flightsResultsFilters;
    }

    private void addOutbounFlightstops(int key) {
        addGeneric(outbounflightstops, String.valueOf(key));
    }
    private void addInbounFlightstops(int key) {
        addGeneric(inbounflightstops, String.valueOf(key));
    }
    private void addCarriers(String key) {
        addGeneric(carriers, key);
    }
    private void addOutboundAirport(String key) {
        addGeneric(outboundAirport, key);
    }
    private void addInboundAirport(String key) {
        addGeneric(inboundAirport, key);
    }

    private void addGeneric(Map<String, Integer> genericMap, String key) {
        if(genericMap.containsKey(key)) {
            Integer count = genericMap.get(key);
            genericMap.put(key, count + 1);
        } else {
            genericMap.put(key, 1);
        }
    }

}
