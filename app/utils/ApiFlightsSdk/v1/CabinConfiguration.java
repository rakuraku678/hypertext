package utils.ApiFlightsSdk.v1;

import play.libs.WS;
import utils.dtos.CabinConfigurationDto;

public class CabinConfiguration extends ApiFlightsSDKBase{

    private static final String ENDPOINT = "/v1/getCabins";

    public static CabinConfigurationDto getByAgency(String slugAgency){
        WS.WSRequest request = prepareRequest(ENDPOINT +"/"+ slugAgency);
        
        return CabinConfigurationDto.parseCabinConfigurationDto(processResponse(request));
    }
}
