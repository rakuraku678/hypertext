package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utils.dtos.Dto;

public class ConfigurationDto implements Dto {

	public String maxMinsForPendingBooking;

	public static ConfigurationDto parseConfigurationDto(JsonElement responseJson) {
		JsonObject jsonObject = responseJson.getAsJsonObject();
		ConfigurationDto configurationDto = new ConfigurationDto();
		configurationDto.maxMinsForPendingBooking= JsonUtils.getStringFromJson(jsonObject, "maxMinsForPendingBooking");

		return configurationDto;
	}

}
