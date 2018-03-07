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
    public boolean showLoginOnSelection;
    public boolean showFFPBox;
    public boolean showNoFeeOnPrice;
    
    public static AgencyDto parseAgencyDto(JsonElement responseJson){
        AgencyDto agencyDto = new AgencyDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        agencyDto.id = JsonUtils.getStringFromJson(jsonObject, "_id");
        agencyDto.slug = JsonUtils.getStringFromJson(jsonObject, "slug");
        agencyDto.name = JsonUtils.getStringFromJson(jsonObject, "name");
        agencyDto.externalId = JsonUtils.getStringFromJson(jsonObject, "externalId");
        agencyDto.active = JsonUtils.getBooleanFromJson(jsonObject, "active");
        agencyDto.defaultAgency = JsonUtils.getBooleanFromJson(jsonObject, "defaultAgency");
        agencyDto.showLoginOnSelection = JsonUtils.getBooleanFromJson(jsonObject, "showLoginOnSelection");
        agencyDto.showFFPBox = JsonUtils.getBooleanFromJson(jsonObject, "showFFPBox");
        agencyDto.showNoFeeOnPrice = JsonUtils.getBooleanFromJson(jsonObject, "showNoFeeOnPrice");

        return agencyDto;
    }
}
