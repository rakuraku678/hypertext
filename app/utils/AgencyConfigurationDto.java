package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.dtos.Dto;

public class AgencyConfigurationDto implements Dto {

	public String template;
	public String faviconURL;
	public String headerHtml;
	public String footerHtml;
	public String customCss;

	public static AgencyConfigurationDto parseAgencyConfigurationDto(JsonElement responseJson) {
		JsonObject jsonObject = responseJson.getAsJsonObject();
		AgencyConfigurationDto agencyConfigurationDto = new AgencyConfigurationDto();
		agencyConfigurationDto.template = JsonUtils.getStringFromJson(jsonObject, "template");
		agencyConfigurationDto.faviconURL = JsonUtils.getStringFromJson(jsonObject, "faviconURL");
		agencyConfigurationDto.headerHtml = JsonUtils.getStringFromJson(jsonObject, "cssHeader");
		agencyConfigurationDto.footerHtml = JsonUtils.getStringFromJson(jsonObject, "cssFooter");
		agencyConfigurationDto.customCss = JsonUtils.getStringFromJson(jsonObject, "customCSS");

		return agencyConfigurationDto;
	}

}
