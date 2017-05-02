package utils.dtos;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
        detailAlternateDatesDto.passengerFare = roundFare(JsonUtils.getStringFromJson(jsonObject, "passengerFare"));
        detailAlternateDatesDto.departureCity = JsonUtils.getStringFromJson(jsonObject, "departureCity");
        detailAlternateDatesDto.returnCity = JsonUtils.getStringFromJson(jsonObject, "returnCity");
        detailAlternateDatesDto.departureDate = JsonUtils.getDateTimeFromJson(jsonObject, "departureDate");
        detailAlternateDatesDto.returnDate = JsonUtils.getDateTimeFromJson(jsonObject, "returnDate");
        detailAlternateDatesDto.flightsCount = JsonUtils.getIntegerFromJson(jsonObject, "flightsCount");

        return detailAlternateDatesDto;
    }
    
    private static String roundFare(String fare){
    	String result = "0";
    	try {
        	if (!Strings.isNullOrEmpty(fare)){
            	Float fareF = Float.valueOf(fare);
            	DecimalFormat df = new DecimalFormat("#");
            	df.setRoundingMode(RoundingMode.CEILING);
            	result = df.format(fareF);
        	}
		} catch (Exception e) {
			return result;
		}
    	
    	return result;
    }
    
}
