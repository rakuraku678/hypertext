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

    public JsonElement statusControl(String id){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + id + "/control");
        return processResponsePost(request);
    }

    public JsonElement confirmation(String id){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + id + "/confirmation");
        return processResponsePost(request);
    }

    public JsonElement get(String id){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + id );
        return processResponsePost(request);
    }
}