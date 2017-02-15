package utils.dtos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import utils.JsonUtils;

public class PromotionDto implements Dto {

    public String id;
    public String slug;
    public String name;
    public AgencyDto agency;
    public boolean active;
    public boolean defaultPromotion;

    public static PromotionDto parsePromotionDto(JsonElement responseJson){
        PromotionDto promotionDto = new PromotionDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        promotionDto.id = JsonUtils.getStringFromJson(jsonObject, "_id");
        promotionDto.slug = JsonUtils.getStringFromJson(jsonObject, "slug");
        promotionDto.name = JsonUtils.getStringFromJson(jsonObject, "name");
        promotionDto.agency = AgencyDto.parseAgencyDto(JsonUtils.getJsonObjectFromJson(jsonObject,"agency"));
        promotionDto.active = JsonUtils.getBooleanFromJson(jsonObject, "active");
        promotionDto.defaultPromotion = JsonUtils.getBooleanFromJson(jsonObject, "defaultPromotion");

        return promotionDto;
    }
}
