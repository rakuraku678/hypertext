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
		System.out.println("//****************************************//");
		System.out.println("//             Agency Conf GET            //");
		System.out.println("//****************************************//");
		System.out.println("// request_url: " + req.url );
		System.out.println("// request_params: " + req.parameters );
		System.out.println("// request_body: " + req.body );


		try {
			HttpResponse response = req.get();
			//String responseStr = response.getString();
			JsonElement responseJson = response.getJson();
			AgencyConfigurationDto agencyConfigurationDto = AgencyConfigurationDto.parseAgencyConfigurationDto(responseJson);

			if (!response.success()) {
				System.out.println("// responde_code: " + response.getStatus());
				System.out.println("// responde: " + responseJson);
				System.out.println("//****************************************//");
				throw new RuntimeException("Api Flights ERROR");
			}
			System.out.println("// jsonResponse: " + responseJson );
			System.out.println("//****************************************//");

			return agencyConfigurationDto;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("Agency Conf process response fail.");
		}
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
