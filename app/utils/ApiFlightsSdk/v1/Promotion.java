package utils.ApiFlightsSdk.v1;

import play.libs.WS;
import utils.dtos.PromotionDto;

public class Promotion extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/promotion";

    public PromotionDto getDefault(){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/default");
        return PromotionDto.parsePromotionDto(processResponse(request));
    }

    public PromotionDto getBySlug(String slug){
        WS.WSRequest request = prepareRequest(ENDPOINT + "/" + slug );
        return PromotionDto.parsePromotionDto(processResponse(request));
    }
}