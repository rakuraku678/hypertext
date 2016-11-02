package utils.ApiFlightsSdk.v1;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import play.libs.WS;

import java.util.Map;
import com.google.common.collect.Maps;

public class Payment extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/payment";

    public String external_id;
    public String agency_id;
    public String expiration;
    public String redirection_url;
    public String callback_url;
    public String currency;
    public String amount;
    public String tax;
    public String subject;
    public String client_name;
    public String promotion;

    public String process(){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        Map<String, Object> mapValues = Maps.newHashMap();

        mapValues.put("external_id",external_id);
        mapValues.put("agency_id", agency_id);
        mapValues.put("expiration", expiration);
        mapValues.put("redirection_url", redirection_url);
        mapValues.put("callback_url", callback_url);
        mapValues.put("currency", currency);
        mapValues.put("amount", amount);
        mapValues.put("tax", tax);
        mapValues.put("subject", subject);
        mapValues.put("client_name", client_name);
        mapValues.put("promotion", promotion);

        String jsonBody = new Gson().toJson(mapValues);

        request.body(jsonBody);

        JsonElement responseJsonObject = processResponsePost(request);

        return responseJsonObject.getAsJsonObject().get("location").getAsString();
    }
}
