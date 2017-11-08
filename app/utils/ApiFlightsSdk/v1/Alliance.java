package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import play.libs.WS;
import utils.dtos.AgencyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Alliance extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/alliance/";

    public static List<String> getFFPWhiteList(String airlineIataCode){
        WS.WSRequest request = prepareRequest(ENDPOINT + "getFFPWhiteList/" + airlineIataCode );
        List<String> FFPwhiteList= new ArrayList();
        JsonElement jsonElement = processResponse(request);
        if (!jsonElement.isJsonNull()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                FFPwhiteList.add(jsonArray.get(i).getAsString());
            }
        }
        return FFPwhiteList;
    }
    public static String getAllianceMessage(String airlineIataCode){
        WS.WSRequest request = prepareRequest(ENDPOINT + "getAllianceMessage/" + airlineIataCode );
        return request.get().getString();
    }
    public static String getAllianceWarningMessage(String airlineIataCode, String errorCode){
        Map dataMap = Maps.newHashMap();
        dataMap.put("errorCode", errorCode);
        dataMap.put("airlineIataCode", airlineIataCode);
        WS.WSRequest request = prepareRequest(ENDPOINT + "getAllianceWarningMessage");
        String jsonBody = new Gson().toJson(dataMap);
        request.body(jsonBody);
        return request.post().getString();
    }
}