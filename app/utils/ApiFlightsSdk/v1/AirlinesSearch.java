package utils.ApiFlightsSdk.v1;

import play.libs.WS;

import com.google.gson.JsonElement;

public class AirlinesSearch extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/airlines";

    public static JsonElement process(String codes){

        WS.WSRequest request = prepareRequest(ENDPOINT+"?airlineCodes="+codes);

        JsonElement responseJsonObject = processResponsePost(request);

        return responseJsonObject;
    }
}
