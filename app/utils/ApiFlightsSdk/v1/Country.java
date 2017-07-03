package utils.ApiFlightsSdk.v1;

import java.util.List;

import play.libs.WS;
import utils.dtos.CountryDto;

public class Country extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/getCountryList";

    public static List<CountryDto> process(){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        return CountryDto.parseAllCountriesDto(processResponse(request));
    }
}