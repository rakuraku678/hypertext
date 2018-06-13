package utils.ApiFlightsSdk.v1;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.libs.WS;

public class Amadeus extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/airsellfromrecommendation";

    public static JsonElement doAirSellRecommendation(JsonElement resultSegmentIda, JsonElement resultSegmentVuelta){
    	WS.WSRequest request = prepareRequest(ENDPOINT);
    	JsonObject jsonParams = new JsonObject();
    	jsonParams.add("resultSegmentIda", resultSegmentIda);
    	jsonParams.add("resultSegmentVuelta", resultSegmentVuelta);
    	
    	
    	request.body(jsonParams);
    	JsonElement response = processResponsePost(request);
    	return response;
    	
    }
}