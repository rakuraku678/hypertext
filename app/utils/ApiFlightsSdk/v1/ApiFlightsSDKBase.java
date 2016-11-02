package utils.ApiFlightsSdk.v1;

import com.google.gson.JsonElement;
import play.Play;
import play.libs.WS;

public abstract class ApiFlightsSDKBase {

    private static final String CLIENT_ID = Play.configuration.getProperty("api.flight.username");
    private static final String CLIENT_SECRET = Play.configuration.getProperty("api.flight.password");
    private static final String ENV_URL = Play.configuration.getProperty("api.flight.url");

    protected static WS.WSRequest prepareRequest(String endpoint) {
        String url = ENV_URL + endpoint;
        System.out.println(url);
        WS.WSRequest request = WS.url(url);
        request.authenticate(CLIENT_ID, CLIENT_SECRET);
        return request;
    }

    protected static JsonElement processResponse(WS.WSRequest request) {
        System.out.println("//****************************************//");
        System.out.println("//               Api Flights              //");
        System.out.println("//****************************************//");
        System.out.println("// request_url: " + request.url );
        System.out.println("// request_params: " + request.parameters );
        System.out.println("// request_body: " + request.body );

        try {
            WS.HttpResponse response = request.get();
            JsonElement jsonResponse = response.getJson();

            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + jsonResponse);
                System.out.println("//****************************************//");
                throw new RuntimeException("Api Flights ERROR");
            }
            System.out.println("// jsonResponse: " + jsonResponse );
            System.out.println("//****************************************//");

            return jsonResponse;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Api Flights process response fail.");
        }
    }

    protected static JsonElement processResponsePost(WS.WSRequest request) {
        System.out.println("//****************************************//");
        System.out.println("//               Api Flights              //");
        System.out.println("//****************************************//");
        System.out.println("// request_url: " + request.url );
        System.out.println("// request_params: " + request.parameters );
        System.out.println("// request_body: " + request.body );

        try {
            WS.HttpResponse response = request.post();
            JsonElement jsonResponse = response.getJson();

            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + jsonResponse);
                System.out.println("//****************************************//");
                throw new RuntimeException("Api Flights ERROR");
            }
            System.out.println("// jsonResponse: " + jsonResponse );
            System.out.println("//****************************************//");

            return jsonResponse;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Api Flights process response fail.");
        }
    }
}
