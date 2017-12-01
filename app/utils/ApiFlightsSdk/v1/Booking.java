package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import play.libs.WS;

import java.util.Map;

public class Booking extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/booking";

    public JsonElement process(JsonElement body){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        request.body(body);

        return processResponsePost(request);
    }

    public JsonElement retryPayment(String pnr, String surname){
        Map dataMap = Maps.newHashMap();
        dataMap.put("pnr", pnr);
        dataMap.put("surname", surname);
        WS.WSRequest request = prepareRequest(ENDPOINT + "/payment/retry");
        String jsonBody = new Gson().toJson(dataMap);
        request.body(jsonBody);
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

    public String promotion(String id){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + id + "/promotion");
        return processResponsePost(request).getAsString();
    }

    public JsonElement get(String id){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + id );
        return processResponsePost(request);
    }
    public JsonElement getByPnr(String pnr){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + pnr );
        return processResponse(request);
    }
}