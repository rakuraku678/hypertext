package models;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class FlightsResultsFilters {
    public Map<String,Integer> outbounflightstops = Maps.newHashMap();
    public Map<String,Integer> inbounflightstops = Maps.newHashMap();
    public Map<String,Integer> carriers = Maps.newHashMap();
    public Map<String,Integer> outboundAirport = Maps.newHashMap();
    public Map<String,Integer> inboundAirport = Maps.newHashMap();
    public Map<String,Integer> carriersNames = Maps.newHashMap();

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

            flightsResultsFilters.addOutboundAirport(firstDepartureSegment.get("departureAirportCode").getAsString());
            flightsResultsFilters.addInboundAirport(firstReturnSegment.get("departureAirportCode").getAsString());
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
