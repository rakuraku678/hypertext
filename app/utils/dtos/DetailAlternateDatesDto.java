package utils.dtos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import utils.JsonUtils;

public class DetailAlternateDatesDto implements Dto {

    public int sequenceNumber;
    public String passengerFare;
    public String departureCity;
    public String returnCity;
    public DateTime departureDate;
    public DateTime returnDate;
    public int flightsCount;

    public static DetailAlternateDatesDto parseDetailAlternateDatesDto(JsonElement responseJson){
        DetailAlternateDatesDto detailAlternateDatesDto = new DetailAlternateDatesDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        detailAlternateDatesDto.sequenceNumber = JsonUtils.getIntegerFromJson(jsonObject, "sequenceNumber");
        detailAlternateDatesDto.passengerFare = JsonUtils.getStringFromJson(jsonObject, "passengerFare");
        detailAlternateDatesDto.departureCity = JsonUtils.getStringFromJson(jsonObject, "departureCity");
        detailAlternateDatesDto.returnCity = JsonUtils.getStringFromJson(jsonObject, "returnCity");
        detailAlternateDatesDto.departureDate = JsonUtils.getDateTimeFromJson(jsonObject, "departureDate");
        detailAlternateDatesDto.returnDate = JsonUtils.getDateTimeFromJson(jsonObject, "returnDate");
        detailAlternateDatesDto.flightsCount = JsonUtils.getIntegerFromJson(jsonObject, "flightsCount");

        return detailAlternateDatesDto;
    }
}
