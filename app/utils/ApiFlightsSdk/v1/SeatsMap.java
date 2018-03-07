package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import play.libs.WS;

import java.util.Map;

public class SeatsMap extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/seats/seatsmap";

    public JsonElement process(JsonElement body){
        WS.WSRequest request = prepareRequest(ENDPOINT);
        request.body(body);
        return processResponsePost(request);
    }

}