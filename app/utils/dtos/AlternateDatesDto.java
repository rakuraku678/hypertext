package utils.dtos;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import utils.JsonUtils;

import java.util.List;

public class AlternateDatesDto implements Dto {

    public String origin;
    public String destination;
    public DateTime departuredate;
    public DateTime returndate;
    public String promotion;
    public List<DetailAlternateDatesDto> detailAlternateDatesDtoList;


    public static AlternateDatesDto parseAlternateDatesDto(JsonElement responseJson) {
        AlternateDatesDto alternateDatesDto = new AlternateDatesDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        alternateDatesDto.origin = JsonUtils.getStringFromJson(jsonObject, "origin");
        alternateDatesDto.destination = JsonUtils.getStringFromJson(jsonObject, "destination");
        alternateDatesDto.promotion = JsonUtils.getStringFromJson(jsonObject, "promotion");
        alternateDatesDto.departuredate = JsonUtils.getDateTimeFromJson(jsonObject, "departuredate");
        alternateDatesDto.returndate = JsonUtils.getDateTimeFromJson(jsonObject, "returndate");

        JsonArray detailAlternateDatesJsonArray = jsonObject.getAsJsonArray("detailAlternateDates");

        alternateDatesDto.detailAlternateDatesDtoList = Lists.newArrayList();
        for (JsonElement jsonElement : detailAlternateDatesJsonArray) {
            alternateDatesDto.detailAlternateDatesDtoList.add(DetailAlternateDatesDto.parseDetailAlternateDatesDto(jsonElement));
        }

        return alternateDatesDto;
    }
}
