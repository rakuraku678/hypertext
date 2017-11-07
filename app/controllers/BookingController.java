package controllers;

import java.io.IOException;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import play.libs.WS;
import play.mvc.Controller;
import play.Play;
import utils.DateUtils;
import play.mvc.Util;
import utils.ApiFlightsSdk.v1.Booking;
import utils.JsonUtils;

public class BookingController extends Controller {
    private static final String APIPAX = Play.configuration.getProperty("apiPax.url");
    private static final String APIALIANZA = Play.configuration.getProperty("apiAlianza.url");

    public static void startBooking(String body) throws IOException {

	    JsonElement bodyJsonElement = new JsonParser().parse(body);
	    JsonArray passengersArray = bodyJsonElement.getAsJsonObject().get("passengers").getAsJsonArray();
	    JsonArray cleanPassengersArray= new JsonArray();
	    for (int i = 0; i < passengersArray.size(); i++) {
	    	String givenName = passengersArray.get(i).getAsJsonObject().get("givenName").getAsString();
	    	String surname = passengersArray.get(i).getAsJsonObject().get("surname").getAsString();
	    	givenName = cleanString(givenName);
	    	surname = cleanString(surname);
	    	if (!checkAllFields(passengersArray,i)){
	    		IOException e = new IOException();
	    		throw e;
	    	}

	    	JsonObject jsonEl = new JsonObject();
	    	jsonEl.addProperty("number", passengersArray.get(i).getAsJsonObject().get("number").getAsString());
	    	jsonEl.addProperty("passengerType", passengersArray.get(i).getAsJsonObject().get("passengerType").getAsString());
	    	jsonEl.addProperty("gender", passengersArray.get(i).getAsJsonObject().get("gender").getAsString());
	    	jsonEl.addProperty("givenName", givenName);
	    	jsonEl.addProperty("surname", surname);
	    	jsonEl.addProperty("foidType", passengersArray.get(i).getAsJsonObject().get("foidType").getAsString());
			jsonEl.addProperty("FFPAirlineIataCode", passengersArray.get(i).getAsJsonObject().get("FFPAirlineIataCode").getAsString());
			jsonEl.addProperty("FFPNumber", passengersArray.get(i).getAsJsonObject().get("FFPNumber").getAsString());

	    	
	    	if (passengersArray.get(i).getAsJsonObject().get("foidType").getAsString().equals("PAS")){
	    		jsonEl.addProperty("nacionalityPassport", passengersArray.get(i).getAsJsonObject().get("nacionalityPassport").getAsString());
	    		jsonEl.addProperty("expirationPassport", DateUtils.reformateDate(passengersArray.get(i).getAsJsonObject().get("expirationPassport").getAsString(),"dd/MM/yyyy","yyyy-MM-dd"));
	    		jsonEl.addProperty("countryPassport", passengersArray.get(i).getAsJsonObject().get("countryPassport").getAsString());
	    		jsonEl.addProperty("foid", passengersArray.get(i).getAsJsonObject().get("foid").getAsString());
	    	}else {
	    		jsonEl.addProperty("foid", passengersArray.get(i).getAsJsonObject().get("foidRut").getAsString());
	    	}
	    	
	    	jsonEl.addProperty("dateOfBirth", DateUtils.reformateDate(passengersArray.get(i).getAsJsonObject().get("dateOfBirth").getAsString(),"dd/MM/yyyy","yyyy-MM-dd"));
	    	cleanPassengersArray.add(jsonEl);
		}
	    
	    bodyJsonElement.getAsJsonObject().remove("passengers");
	    bodyJsonElement.getAsJsonObject().add("passengers", cleanPassengersArray);
	    
	    Booking booking = new Booking();
	
	//    Map params = Maps.newHashMap();
	//    params.put("id", "_ID_");
	
	//    bodyJsonElement.getAsJsonObject().addProperty("redirect", Router.getFullUrl("PaymentFlowController.processPayment", params));
	    String redirectRouter = bodyJsonElement.getAsJsonObject().get("origin").getAsString() + "/checkout/_ID_/process";
	    bodyJsonElement.getAsJsonObject().addProperty("redirect", redirectRouter);
	
	    renderJSON(booking.process(bodyJsonElement));	
    }


    public static void statusBooking() {
        Booking booking = new Booking();
        renderJSON(booking.statusControl(params.get("id")));
    }
    public static String cleanString(String input) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }
    public static boolean checkAllFields(JsonArray passengersArray, int pNumber){
		try {
	    	String givenName = passengersArray.get(pNumber).getAsJsonObject().get("givenName").getAsString();
	    	String surname = passengersArray.get(pNumber).getAsJsonObject().get("surname").getAsString();
			String number = passengersArray.get(pNumber).getAsJsonObject().get("number").getAsString();
			String passengerType = passengersArray.get(pNumber).getAsJsonObject().get("passengerType").getAsString();
			String gender = passengersArray.get(pNumber).getAsJsonObject().get("gender").getAsString();
			String foidType = passengersArray.get(pNumber).getAsJsonObject().get("foidType").getAsString();
			String nacionalityPassport = passengersArray.get(pNumber).getAsJsonObject().get("nacionalityPassport").getAsString();
			String expirationPassport = passengersArray.get(pNumber).getAsJsonObject().get("expirationPassport").getAsString();
			String countryPassport = passengersArray.get(pNumber).getAsJsonObject().get("countryPassport").getAsString();
			String foid = passengersArray.get(pNumber).getAsJsonObject().get("foid").getAsString();
			String foidRut = "";
			if (!foidType.equals("PAS") ){
				foidRut = passengersArray.get(pNumber).getAsJsonObject().get("foidRut").getAsString();
			}
			String dateOfBirth = passengersArray.get(pNumber).getAsJsonObject().get("dateOfBirth").getAsString();
			
			if (foidType.equals("PAS") && (Strings.isNullOrEmpty(foid) || Strings.isNullOrEmpty(nacionalityPassport) || Strings.isNullOrEmpty(expirationPassport) || Strings.isNullOrEmpty(countryPassport)) ){
				System.out.println("Campo vacio con Pasaporte: "+foidType);
				System.out.println(foid);
				System.out.println(nacionalityPassport);
				System.out.println(expirationPassport);
				System.out.println(countryPassport);
				return false;
			}
			else if (
					!foidType.equals("PAS") && (Strings.isNullOrEmpty(givenName) || Strings.isNullOrEmpty(surname) || Strings.isNullOrEmpty(number) ||
					Strings.isNullOrEmpty(passengerType) || Strings.isNullOrEmpty(gender) ||
					Strings.isNullOrEmpty(foidRut) || Strings.isNullOrEmpty(dateOfBirth) )
					) {
				System.out.println("Campo vacio con RUT: "+foidType);
				System.out.println(givenName);
				System.out.println(surname);
				System.out.println(number);
				System.out.println(passengerType);
				System.out.println(gender);
				System.out.println(foidRut);
				System.out.println(dateOfBirth);
				return false;
			}
			
			return true;
			 
		} catch (Exception e) {
			System.out.println("expecion rara");
			e.printStackTrace();
			return false;
		}
    }
	private static JsonObject getTokenByRut(String rut){
		String url = APIPAX + rut;
		System.out.println(url);
		WS.WSRequest request = WS.url(url);
		request.setHeader("Authorization","Basic a2R1OmtkdQ==");
		WS.HttpResponse response = request.get();
		JsonElement jsonResponse = response.getJson();
		return(jsonResponse.getAsJsonObject());
	}
	public static void getPaxByToken(String body){
		String token = params.get("token");
		String url = APIALIANZA+ token;
		System.out.println(url);
		WS.WSRequest request = WS.url(url);
		request.setHeader("Authorization","Basic a2R1OmtkdQ==");
		WS.HttpResponse response = request.get();
		JsonElement jsonResponse = response.getJson();
		renderJSON(jsonResponse.getAsJsonObject());
	}
	public static void getFFPNumberByRut(){
        String rut = params.get("rut");
        String url = APIALIANZA+ rut;
        System.out.println(url);
        WS.WSRequest request = WS.url(url);
        WS.HttpResponse response = request.get();
        String valueToEscape = response.getString();
        if (valueToEscape!= "" && valueToEscape != null) {
            int startIndex = valueToEscape.indexOf(",\"mensaje\":\"Estimado");
            String searchString = "}";
            try {
                int endIndex = startIndex + valueToEscape.substring(startIndex).indexOf(searchString);
                String toBeReplaced = valueToEscape.substring(startIndex, endIndex);
                valueToEscape = valueToEscape.replace(toBeReplaced, "");

            }catch (StringIndexOutOfBoundsException e){
                valueToEscape = "{\"status\":\"0\"}";
            }

            JsonObject jsonObject = new JsonParser().parse(valueToEscape).getAsJsonObject();
            jsonObject.addProperty("status","1");

            renderJSON(jsonObject);
        }
    }
}