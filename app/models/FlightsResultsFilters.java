package models;

import java.util.List;
import java.util.Map;

import utils.ApiFlightsSdk.v1.AirlinesSearch;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controllers.SearchController;
import utils.ApiFlightsSdk.v1.Airport;
import utils.dtos.AirportDto;

public class FlightsResultsFilters {
    public Map<String,Integer> outbounflightstops = Maps.newHashMap();
    public Map<String,Integer> inbounflightstops = Maps.newHashMap();
    public Map<String,Integer> carriers = Maps.newHashMap();
    public Map<String,Integer> mktcarriers = Maps.newHashMap();
    public Map<String,Integer> outboundAirport = Maps.newHashMap();
    public Map<String,Integer> inboundAirport = Maps.newHashMap();
    public Map<String,Integer> carriersNames = Maps.newHashMap();
    public Map<String,String> carriersCodesXNames = Maps.newHashMap();
    public Map<String,String> mktCarriersCodesXNames = Maps.newHashMap();
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
                flightsResultsFilters.addMktCarriers(departureSegmentsObject.get("marketingAirline").getAsString());
            }
            for(JsonElement returnSegmentsElement : returnSegmentDetail){
                JsonObject returnSegmentsObject = returnSegmentsElement.getAsJsonObject();
                flightsResultsFilters.addCarriers(returnSegmentsObject.get("operatingAirlineCode").getAsString());
                flightsResultsFilters.addMktCarriers(returnSegmentsObject.get("marketingAirline").getAsString());
            }
            
            
            JsonObject firstDepartureSegment = departureSegmentDetail.get(0).getAsJsonObject();
            JsonObject firstReturnSegment = returnSegmentDetail.get(0).getAsJsonObject();

            
            String departureAirportCode = firstDepartureSegment.get("departureAirportCode").getAsString();
            AirportDto departureAirportDto = new Airport().getByIataCode(departureAirportCode);
            flightsResultsFilters.addOutboundAirport(departureAirportDto.name);
            flightsResultsFilters.outboundAirportCodes.put(departureAirportDto.name,departureAirportCode);

            String returnAirportCode = firstReturnSegment.get("departureAirportCode").getAsString();
            AirportDto returnAirportDto = new Airport().getByIataCode(returnAirportCode);
            flightsResultsFilters.addInboundAirport(returnAirportDto.name);
            flightsResultsFilters.inboundAirportCodes.put(returnAirportDto.name, returnAirportCode);
        }
        //Operating Carrier
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

        //Marketing Carrier
        StringBuilder mktAirlineCodes = new StringBuilder();
        for (Map.Entry<String, Integer> entry : flightsResultsFilters.mktcarriers.entrySet())
        {
            mktAirlineCodes.append(entry.getKey()+",");
        }

        JsonElement jsonMkt = AirlinesSearch.process(mktAirlineCodes.toString());

        if (!jsonMc.isJsonNull()){
            flightsResultsFilters.mktCarriersCodesXNames = Maps.newHashMap();
            JsonArray jArrayMkt = jsonMkt.getAsJsonArray();
            for (int i = 0; i < jArrayMkt.size(); i++) {
                String code = jArrayMkt.get(i).getAsJsonObject().get("iataCode").getAsString();
                String name = jArrayMkt.get(i).getAsJsonObject().get("name").getAsString();
                flightsResultsFilters.mktCarriersCodesXNames.put(code, name);

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
    private void addMktCarriers(String key) {
        addGeneric(mktcarriers, key);
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
