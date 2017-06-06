package utils.dtos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.JsonUtils;
import java.io.Serializable;


public class AirportDto implements Dto,Serializable {

    public String name;
    public String ranking;
    public String latitude;
    public String longitude;
    public String country;
    public String countryName;
    public String state;
    public String stateName;
    public String city;
    public String id;
    public String iataCityCode;
    public String isCity;
    public String isAirport;
    public String isUnknown;

    public static AirportDto parseAiportDto(JsonElement responseJson){
        AirportDto airportDto = new AirportDto();
        JsonObject jsonObject = responseJson.getAsJsonObject();
        airportDto.name = JsonUtils.getStringFromJson(jsonObject, "name");
        airportDto.ranking = JsonUtils.getStringFromJson(jsonObject, "ranking");
        airportDto.latitude = JsonUtils.getStringFromJson(jsonObject, "latitude");
        airportDto.longitude = JsonUtils.getStringFromJson(jsonObject, "longitude");
        airportDto.country = JsonUtils.getStringFromJson(jsonObject, "country");
        airportDto.countryName = JsonUtils.getStringFromJson(jsonObject, "countryName");
        airportDto.state = JsonUtils.getStringFromJson(jsonObject, "state");
        airportDto.stateName = JsonUtils.getStringFromJson(jsonObject, "stateName");
        airportDto.city = JsonUtils.getStringFromJson(jsonObject, "city");
        airportDto.id = JsonUtils.getStringFromJson(jsonObject, "id");
        airportDto.iataCityCode = JsonUtils.getStringFromJson(jsonObject,"iataCityCode");
        airportDto.isCity = JsonUtils.getStringFromJson(jsonObject, "isCity");
        airportDto.isAirport = JsonUtils.getStringFromJson(jsonObject, "isAirport");
        airportDto.isUnknown = JsonUtils.getStringFromJson(jsonObject, "isUnknown");

        return airportDto;
    }
}