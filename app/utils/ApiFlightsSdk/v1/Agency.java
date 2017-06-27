package utils.ApiFlightsSdk.v1;

import play.libs.WS;
import utils.dtos.AgencyDto;

public class Agency extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/agency";

    public AgencyDto getBySlug(String slugAgency){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + slugAgency );
        return AgencyDto.parseAgencyDto(processResponse(request));
    }
}