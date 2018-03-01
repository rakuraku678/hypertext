package utils;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import play.libs.WS;
import utils.ApiFlightsSdk.v1.ApiFlightsSDKBase;
import utils.dtos.Dto;

public class SearchConfigurationDto extends ApiFlightsSDKBase implements Dto {

	private static final String ENDPOINT = "/v1/getSearchConfiguration";
	
	public int minDaysAP;
	public int maxDaysAP;

    public static SearchConfigurationDto getSearchConfiguration(){
        WS.WSRequest request = prepareRequest(ENDPOINT);
        
        return SearchConfigurationDto.parseSearchConfigurationDto(processResponse(request));
    }
    
    public static SearchConfigurationDto parseSearchConfigurationDto(JsonElement responseJson){

    	SearchConfigurationDto searchConfigurationDto = new SearchConfigurationDto();
    	searchConfigurationDto.minDaysAP = responseJson.getAsJsonObject().get("minDaysAP").getAsInt();
    	searchConfigurationDto.maxDaysAP = responseJson.getAsJsonObject().get("maxDaysAP").getAsInt();
        return searchConfigurationDto;
    }
	
}
