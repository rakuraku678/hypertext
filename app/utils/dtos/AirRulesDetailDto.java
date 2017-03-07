package utils.dtos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.JsonUtils;

public class AirRulesDetailDto implements Dto {

    public String rph;
    public String title;
    public String text;

    public static AirRulesDetailDto parseAirRulesDto(JsonElement responseJson){
        AirRulesDetailDto airRulesDetailDto = new AirRulesDetailDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        airRulesDetailDto.rph = JsonUtils.getStringFromJson(jsonObject, "rph");
        airRulesDetailDto.title = JsonUtils.getStringFromJson(jsonObject, "title");
        airRulesDetailDto.text = JsonUtils.getStringFromJson(jsonObject, "text");
        return airRulesDetailDto;
    }
}