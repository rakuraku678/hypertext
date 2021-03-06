package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import play.libs.WS;

import java.util.List;
import java.util.Map;

public class FlightsAltenateDates extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/flights/altenatedates";

    public String origin;
    public String destination;
    public String departuredate;
    public String returndate;
    public String promotion;
    public String cabin;
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
        mapValues.put("cabin",cabin);

        String jsonBody = new Gson().toJson(mapValues);

        request.body(jsonBody);

        JsonElement responseJsonObject = processResponse(request);

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

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public void addPassengerType(String passengerType, String quantity) {
        if(!"0".equals(quantity)) {
            Map<String, String> passengerTypeMap = Maps.newHashMap();
            passengerTypeMap.put("code", passengerType);
            passengerTypeMap.put("quantity", quantity);
            passengerTypeList.add(passengerTypeMap);
        }
    }
}
