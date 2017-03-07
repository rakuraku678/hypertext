package utils.dtos;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.JsonUtils;

import java.util.List;

public class AirRulesDto implements Dto {

    public String hostCommand;
    public List<AirRulesDetailDto> rules = Lists.newArrayList();

    public static AirRulesDto parseAirRulesDto(JsonElement responseJson){
        AirRulesDto airRulesDto = new AirRulesDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        airRulesDto.hostCommand = JsonUtils.getStringFromJson(jsonObject, "hostCommand");

        for (JsonElement ruleJsonElement : JsonUtils.getJsonArrayFromJson(jsonObject, "rules")) {
            JsonObject ruleJsonObject = ruleJsonElement.getAsJsonObject();
            airRulesDto.rules.add(AirRulesDetailDto.parseAirRulesDto(ruleJsonObject));
        }

        return airRulesDto;
    }
}