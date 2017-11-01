package utils.ApiFlightsSdk.v1;

import play.cache.Cache;
import play.libs.WS;
import utils.dtos.AirportDto;

public class Airport extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/airport";

    public AirportDto getByIataCode(String iatacode){
        final String cacheKey = "airport_" + iatacode;
        AirportDto airport = Cache.get(cacheKey, AirportDto.class);
        if (airport == null) {
            WS.WSRequest request = prepareRequest(ENDPOINT + "/" + iatacode );
            airport = AirportDto.parseAiportDto(processResponse(request));
            Cache.set(cacheKey, airport, "1d");
        }
        return airport;
    }
}