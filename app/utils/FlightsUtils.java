package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.ApiFlightsSdk.v1.Promotion;

public class FlightsUtils {

	public static ConfigurationDto getConfiguration() {
		String url = Play.configuration.getProperty("flights.url");
		WSRequest request = WS.url(url + "/getconfiguration");
		request.authenticate("admin", "swrt4h5sgef3");
		try {
			HttpResponse response = request.get();
			JsonElement responseJson = response.getJson();
			ConfigurationDto configurationDto = ConfigurationDto.parseConfigurationDto(responseJson);

			if (!response.success()) {
				System.out.println("// responde_code: " + response.getStatus());
				System.out.println("// responde: " + responseJson);
				System.out.println("//****************************************//");
				throw new RuntimeException("Flights ERROR");
			}
			System.out.println("// jsonResponse: " + responseJson );
			System.out.println("//****************************************//");

			return configurationDto;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("Flights Configuration process response fail.");
		}
	}
	
}
