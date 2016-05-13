package utils;

import com.google.gson.JsonElement;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

public class TravelClubUtils {

	private static final String URL = Play.configuration
			.getProperty("api.agency.url");

	public static AgencyConfigurationDto getAgencyConfiguration(String agencyId) {
		WSRequest req = WS.url(URL);
		req.authenticate("E3ra79", "api33-33a");
		req.setParameter("agencyId", agencyId);
		HttpResponse response = req.get();
		//String responseStr = response.getString();
		JsonElement responseJson = response.getJson();
		AgencyConfigurationDto agencyConfigurationDto = AgencyConfigurationDto.parseAgencyConfigurationDto(responseJson);
		return agencyConfigurationDto;
	}
}
