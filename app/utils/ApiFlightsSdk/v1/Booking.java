package utils.ApiFlightsSdk.v1;

import com.google.gson.JsonElement;
import play.libs.WS;

public class Booking extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/booking";

    public JsonElement process(JsonElement body){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        request.body(body);

        return processResponsePost(request);
    }
}