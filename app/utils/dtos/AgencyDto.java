package utils.dtos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.JsonUtils;

public class AgencyDto implements Dto {

    public String id;
    public String slug;
    public String name;
    public String externalId;
    public boolean active;
    public boolean defaultAgency;

    public static AgencyDto parseAgencyDto(JsonElement responseJson){
        AgencyDto promotionDto = new AgencyDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        promotionDto.id = JsonUtils.getStringFromJson(jsonObject, "_id");
        promotionDto.slug = JsonUtils.getStringFromJson(jsonObject, "slug");
        promotionDto.name = JsonUtils.getStringFromJson(jsonObject, "name");
        promotionDto.externalId = JsonUtils.getStringFromJson(jsonObject, "externalId");
        promotionDto.active = JsonUtils.getBooleanFromJson(jsonObject, "active");
        promotionDto.defaultAgency = JsonUtils.getBooleanFromJson(jsonObject, "defaultAgency");

        return promotionDto;
    }
}
