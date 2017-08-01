package utils.ApiFlightsSdk.v1;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.libs.WS;

public class BFMSearch extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/flights/bfm";

    public String origin;
    public String destination;
    public String departuredate;
    public String returndate;
    public String country;
    public String city;
    public String promotion;
    public String dollarExchangeRate;
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
        mapValues.put("dollarExchangeRate",dollarExchangeRate);

        String jsonBody = new Gson().toJson(mapValues);

        request.body(jsonBody);

        JsonElement responseJsonObject = processResponse(request);

        responseJsonObject = postProcessConnectionHours(responseJsonObject);
        
        return responseJsonObject;
    }

    private JsonElement postProcessConnectionHours(JsonElement responseJsonObject) {
    	
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    	int i = 0;
    	int j = 0;
    	Gson gson = new Gson();
    	Map mapTime = Maps.newHashMap();
    	mapTime.put("longConnection", true);
    	JsonElement jMapElement = gson.toJsonTree(mapTime);
    	String arrivalTime = "";
    	String departureTime = "";
    	
    	for (JsonElement jsonElement : responseJsonObject.getAsJsonArray()) {
            JsonObject flightsResult = jsonElement.getAsJsonObject();
	    	JsonObject departureSegment = flightsResult.getAsJsonObject("departureSegment");
	    	JsonObject returnSegment = flightsResult.getAsJsonObject("returnSegment");
	    	JsonArray departureSegmentDetail = departureSegment.getAsJsonArray("detail");
	    	JsonArray returnSegmentDetail = returnSegment.getAsJsonArray("detail");
	    	
	    	//Calculo tiempos de conexion para la ida
	    	if (departureSegmentDetail.size()>1){
		    	j = 0;
		    	arrivalTime = departureSegmentDetail.get(0).getAsJsonObject().get("arrivalDateTime").getAsString();
		    	for(JsonElement departureSegmentsElement : departureSegmentDetail){
		    		if (j>0) {
			    		departureTime = departureSegmentsElement.getAsJsonObject().get("departureDateTime").getAsString();
			    		DateTime arrivalDateTime = formatter.parseDateTime(arrivalTime);
			    		DateTime departureDateTime = formatter.parseDateTime(departureTime);
			    		int hourDiff = Hours.hoursBetween(arrivalDateTime, departureDateTime).getHours();
			    		if (hourDiff>=5){
		                	responseJsonObject.getAsJsonArray().get(i).getAsJsonObject().getAsJsonObject("departureSegment").getAsJsonArray("detail").get(j-1).getAsJsonObject().add("extraData", jMapElement);
			    		}
			    		arrivalTime = departureSegmentDetail.get(j).getAsJsonObject().get("arrivalDateTime").getAsString();
		    		}
		    		j++;
		    	}
	    	}
	    	
	    	//Calculo tiempos de conexion para la vuelta
	    	if (returnSegmentDetail.size()>1){
		    	j = 0;
		    	arrivalTime = returnSegmentDetail.get(0).getAsJsonObject().get("arrivalDateTime").getAsString();
		    	for(JsonElement returnSegmentsElement : returnSegmentDetail){
		    		if (j>0) {
			    		departureTime = returnSegmentsElement.getAsJsonObject().get("departureDateTime").getAsString();
			    		DateTime arrivalDateTime = formatter.parseDateTime(arrivalTime);
			    		DateTime departureDateTime = formatter.parseDateTime(departureTime);
			    		int hourDiff = Hours.hoursBetween(arrivalDateTime, departureDateTime).getHours();
			    		if (hourDiff>=5){
		                	responseJsonObject.getAsJsonArray().get(i).getAsJsonObject().getAsJsonObject("returnSegment").getAsJsonArray("detail").get(j-1).getAsJsonObject().add("extraData", jMapElement);
			    		}
			    		arrivalTime = returnSegmentDetail.get(j).getAsJsonObject().get("arrivalDateTime").getAsString();
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
    
    public void setDollarExchangeRate(String rate) {
        this.dollarExchangeRate = rate;
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
