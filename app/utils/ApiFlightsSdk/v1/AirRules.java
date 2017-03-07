package utils.ApiFlightsSdk.v1;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import play.libs.WS;
import utils.dtos.AirRulesDto;

import java.util.Map;

public class AirRules extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/flights/air/rules";

    public String ticketingDate;
    public String travelDate;
    public String origin;
    public String destination;
    public String marketingCarrier;
    public String fareBasis;

    public AirRulesDto process(){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        Map<String, Object> mapValues = Maps.newHashMap();

        mapValues.put("ticketingDate",ticketingDate);
        mapValues.put("travelDate",travelDate);
        mapValues.put("origin",origin);
        mapValues.put("destination",destination);
        mapValues.put("marketingCarrier",marketingCarrier);
        mapValues.put("fareBasis",fareBasis);
        String jsonBody = new Gson().toJson(mapValues);

        request.body(jsonBody);

        return AirRulesDto.parseAirRulesDto(processResponse(request));
    }
}