package utils.dtos;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import utils.JsonUtils;

public class CountryDto implements Dto {

    public String code;
    public String name;

    public static List<CountryDto> parseAllCountriesDto(JsonElement responseJson){
    	List<CountryDto> countries = Lists.newArrayList();
        JsonArray jsonOArray = responseJson.getAsJsonArray();
        for (int i = 0; i < jsonOArray.size(); i++) {
        	CountryDto countryDto = new CountryDto();
        	countryDto.code = JsonUtils.getStringFromJson(jsonOArray.get(i).getAsJsonObject(), "code");
        	countryDto.name = JsonUtils.getStringFromJson(jsonOArray.get(i).getAsJsonObject(), "name");
            countries.add(countryDto);
        }

        return countries;
    }
}
