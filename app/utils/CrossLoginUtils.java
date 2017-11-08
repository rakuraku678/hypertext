package utils;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dto.StateDto;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

public class CrossLoginUtils {
    private static final String ENDPOINT = Play.configuration.getProperty("crossLoginApi.url");
    private static final String APPNAME = "vuelos";

    public static String getTransactionToken(String agencyId, String password) {
        WSRequest req = WS.url(ENDPOINT+"/transaction/v1/generateToken");
        req.setHeader("Content-Type", "application/json");
        Map<String,String> dataMap = Maps.newHashMap();
        dataMap.put("agencyId", agencyId);
        dataMap.put("appName", APPNAME);
        dataMap.put("password", password);
        String json = new Gson().toJson(dataMap);
        req.body(json);
        
        try {
        	System.out.println("enviando request a: "+ENDPOINT+"/transaction/v1/generateToken");
        	System.out.println(json.toString());
            HttpResponse response = req.post();
            JsonElement responseJson = response.getJson();
            JsonObject jsonObject = responseJson.getAsJsonObject();
            String token = JsonUtils.getStringFromJson(jsonObject, "token");

            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + responseJson.toString());
                System.out.println("//****************************************//");
                throw new RuntimeException("Crosslogin service ERROR");
            }
            System.out.println("// jsonResponse: " + responseJson.toString() );
            System.out.println("//****************************************//");

            return token;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("CrossLogin getToken fail.");
        }
    }

    public static StateDto registerClient(String token, String clientName) {
        WSRequest req = WS.url(ENDPOINT+"/transaction/v1/clientNameRegisterRequest");
        req.setHeader("Content-Type", "application/json");
        Map<String,String> dataMap = Maps.newHashMap();
        dataMap.put("token", token);
        dataMap.put("clientName", clientName);
        String json = new Gson().toJson(dataMap);
        req.body(json);
        
        try {
            HttpResponse response = req.post();
            JsonElement responseJson = response.getJson();
            StateDto stateDto = StateDto.parseStateDto(responseJson);
            
            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + responseJson.toString());
                System.out.println("//****************************************//");
                throw new RuntimeException("CrossLogin service ERROR");
            }
            System.out.println("// jsonResponse: " + responseJson.toString() );
            System.out.println("//****************************************//");

            return stateDto;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("CrossLogin registerClient fail.");
        }
    }
    
    public static StateDto getState(String token) {
        WSRequest req = WS.url(ENDPOINT+"/transaction/v1/getState");
        req.setHeader("Content-Type", "application/json");
        
        Map dataMap = Maps.newHashMap();
        dataMap.put("appToken", token);
        String json = new Gson().toJson(dataMap);
        req.body(json);
        
        try {
            HttpResponse response = req.post();
            JsonElement responseJson = response.getJson();
            System.out.println("// jsonResponse: " + responseJson.toString() );
            System.out.println("//****************************************//");
            
            StateDto stateDto = StateDto.parseStateDto(responseJson);
            
            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + responseJson.toString());
                System.out.println("//****************************************//");
                throw new RuntimeException("CrossLogin service ERROR");
            }
           

            return stateDto;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("CrossLogin getState fail.");
        }
    }
    
    public static Map<String,String> getBchToken(String state, String clientId) {
        WSRequest req = WS.url(ENDPOINT+"/transaction/v1/bchTokenRequest");
        req.setHeader("Content-Type", "application/json");
        
        Map dataMap = Maps.newHashMap();
        dataMap.put("state", state);
        dataMap.put("clientId", clientId);
        String json = new Gson().toJson(dataMap);
        req.body(json);
        Map<String,String> responseMap = Maps.newHashMap();
        try {
            HttpResponse response = req.post();
            JsonElement responseJson = response.getJson();
            JsonObject jsonObject = responseJson.getAsJsonObject();
            String bchToken = JsonUtils.getStringFromJson(jsonObject, "bchToken");
            String appToken = JsonUtils.getStringFromJson(jsonObject, "appToken");
            responseMap.put("bchToken", bchToken);
            responseMap.put("appToken", appToken);
            
            if (!response.success()) {
                System.out.println("// responde_code: " + response.getStatus());
                System.out.println("// responde: " + responseJson.toString());
                System.out.println("//****************************************//");
                throw new RuntimeException("CrossLogin service ERROR");
            }
            System.out.println("// jsonResponse: " + responseJson.toString() );
            System.out.println("//****************************************//");

            return responseMap;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("CrossLogin getBchToken fail.");
        }
    }
    
}
