package dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utils.JsonUtils;

public class StateDto {

    public String 	appToken;
    public Integer 	appTokenExpirationTime;
    public String 	paymentToken;
    public Integer  paymentTokenExpirationTime;
    public String   bchToken;
    public String   bchTokenExpirationTime;
    public String   clientRut;
    public String	clientName;
    public String	stateBch;

	public static StateDto parseStateDto(JsonElement responseJson) {
		JsonObject jsonObject = responseJson.getAsJsonObject();
		StateDto stateDto = new StateDto();
		stateDto.appToken = JsonUtils.getStringFromJson(jsonObject, "appToken");
		stateDto.appTokenExpirationTime = JsonUtils.getIntegerFromJson(jsonObject, "appTokenExpirationTime");
		stateDto.paymentToken = JsonUtils.getStringFromJson(jsonObject, "paymentToken");
		stateDto.paymentTokenExpirationTime = JsonUtils.getIntegerFromJson(jsonObject, "paymentTokenExpirationTime");
		stateDto.bchToken = JsonUtils.getStringFromJson(jsonObject, "bchToken");
		stateDto.bchTokenExpirationTime = JsonUtils.getStringFromJson(jsonObject, "bchTokenExpirationTime");
		stateDto.clientRut = JsonUtils.getStringFromJson(jsonObject, "clientRut");
		stateDto.clientName = JsonUtils.getStringFromJson(jsonObject, "clientName");
		stateDto.stateBch = JsonUtils.getStringFromJson(jsonObject, "stateBch");
		
		return stateDto;
	}
}