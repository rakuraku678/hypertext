package utils.ApiFlightsSdk.v1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import play.libs.WS;
import utils.dtos.AgencyDto;

import java.util.ArrayList;
import java.util.List;

public class Alliance extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/getFFPWhiteList";

    public static List<String> getFFPWhiteList(String AirlineIataCode){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + AirlineIataCode );
        List<String> FFPwhiteList= new ArrayList();
        JsonElement jsonElement = processResponse(request);
        System.out.println(jsonElement.toString());
        if (!jsonElement.isJsonNull()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                FFPwhiteList.add(jsonArray.get(i).getAsString());
            }
        }
        return FFPwhiteList;
    }
}