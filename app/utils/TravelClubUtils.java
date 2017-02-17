package utils;

import com.google.gson.JsonElement;

import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.ApiFlightsSdk.v1.Promotion;

public class TravelClubUtils {

	private static final String AGENCY_URL = Play.configuration.getProperty("api.agency.url");
	private static final String DOLLAR_EXCHANGE_RATE_URL = Play.configuration.getProperty("api.dollar.url");

	public static AgencyConfigurationDto getAgencyConfiguration(String agencyId) {
		WSRequest req = WS.url(AGENCY_URL);
		req.authenticate("E3ra79", "api33-33a");
		req.setParameter("agencyId", agencyId);
		HttpResponse response = req.get();
		//String responseStr = response.getString();
		JsonElement responseJson = response.getJson();
		AgencyConfigurationDto agencyConfigurationDto = AgencyConfigurationDto.parseAgencyConfigurationDto(responseJson);
		return agencyConfigurationDto;
	}
	

	public static String getDollarExchangeRate() {

        String agencyId = new Promotion().getDefault().agency.externalId;
        System.out.println("request to: "+DOLLAR_EXCHANGE_RATE_URL+"/"+agencyId);
        
		WSRequest req = WS.url(DOLLAR_EXCHANGE_RATE_URL+"/"+agencyId);
		req.authenticate("E3ra79", "api33-33a");
		HttpResponse response = req.get();
		JsonElement responseJson = response.getJson();
		System.out.println("response dollar api: "+responseJson.toString());
		
		if (responseJson.getAsJsonObject().get("error").getAsString().equals("OK")){
			System.out.println("es ok: "+responseJson.getAsJsonObject().get("value").getAsString());
			return responseJson.getAsJsonObject().get("value").getAsString();
		}
		else {
			System.out.println("NOK");
			return responseJson.getAsJsonObject().get("error").getAsString();
		}
	}
	
}
