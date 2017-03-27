package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.libs.WS;

import java.util.List;
import java.util.Map;

public class BFMSearch extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/flights/bfm";

    public String origin;
    public String destination;
    public String departuredate;
    public String returndate;
    public String country;
    public String city;
    public String promotion;
    public List<Map<String,String>> passengerTypeList = Lists.newArrayList();

    public JsonElement process(){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        Map<String, Object> mapValues = Maps.newHashMap();

        mapValues.put("origin",origin);
        mapValues.put("destination",destination);
        mapValues.put("departuredate",departuredate);
        mapValues.put("returndate",returndate);
        mapValues.put("passengers",passengerTypeList);
        mapValues.put("promotion",promotion);
        mapValues.put("country",country);
        mapValues.put("city",city);

        String jsonBody = new Gson().toJson(mapValues);

        request.body(jsonBody);

        JsonElement responseJsonObject = processResponse(request);

        responseJsonObject = postProcessDeparture(responseJsonObject);
        return responseJsonObject;
    }

    private JsonElement postProcessDeparture(JsonElement responseJsonObject) {
    	
    	int i = 0;
    	int j = 0;
    	int elapsedTime = 0;
    	Map mapTime = Maps.newHashMap();
    	Gson gson = new Gson();
    	
    	for (JsonElement jsonElement : responseJsonObject.getAsJsonArray()) {
            JsonObject flightsResult = jsonElement.getAsJsonObject();
	    	JsonObject departureSegment = flightsResult.getAsJsonObject("departureSegment");
	    	JsonObject returnSegment = flightsResult.getAsJsonObject("returnSegment");
	    	JsonArray departureSegmentDetail = departureSegment.getAsJsonArray("detail");
	    	JsonArray returnSegmentDetail = returnSegment.getAsJsonArray("detail");
	    	
	    	//Calculo tiempos de conexion para la ida
	    	if (departureSegmentDetail.size()>1){
		    	j = 0;
		    	elapsedTime = 0;
		    	mapTime = Maps.newHashMap();
		    	for(JsonElement departureSegmentsElement : departureSegmentDetail){
		    		if (j+1<departureSegmentDetail.size()) {
		                elapsedTime = elapsedTime + departureSegmentsElement.getAsJsonObject().get("elapsedTime").getAsInt();
		                if (elapsedTime>=300){
		                	mapTime.put("longConnection", true);
		                	JsonElement jElement = gson.toJsonTree(mapTime);
		                	responseJsonObject.getAsJsonArray().get(i).getAsJsonObject().getAsJsonObject("departureSegment").getAsJsonArray("detail").get(j).getAsJsonObject().add("extraData", jElement);
		                	break;
		                }
		    		}
		    		j++;
	            }
	    	}
	    	//Calculo tiempos de conexion para la vuelta
	    	if (returnSegmentDetail.size()>1){
		    	j = 0;
		    	elapsedTime = 0;
		    	mapTime = Maps.newHashMap();
		    	for(JsonElement returnSegmentsElement : returnSegmentDetail){
		    		if (j+1<returnSegmentDetail.size()) {
		                elapsedTime = elapsedTime + returnSegmentsElement.getAsJsonObject().get("elapsedTime").getAsInt();
		                if (elapsedTime>=300){
		                	mapTime.put("longConnection", true);
		                	JsonElement jElement = gson.toJsonTree(mapTime);
		                	responseJsonObject.getAsJsonArray().get(i).getAsJsonObject().getAsJsonObject("returnSegment").getAsJsonArray("detail").get(j).getAsJsonObject().add("extraData", jElement);
		                	break;
		                }
		    		}
		    		j++;
	            }
	    	}
        	i++;
    	}
		return responseJsonObject;
	}

	public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDeparturedate(String departuredate) {
        this.departuredate = departuredate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public void addPassengerType(String passengerType, String quantity) {
        if(!"0".equals(quantity)) {
            Map<String, String> passengerTypeMap = Maps.newHashMap();
            passengerTypeMap.put("code", passengerType);
            passengerTypeMap.put("quantity", quantity);
            passengerTypeList.add(passengerTypeMap);
        }
    }

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
    
}
