package controllers;

import java.util.Map;

import play.mvc.Controller;
import play.mvc.Router;
import utils.ApiFlightsSdk.v1.Booking;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BookingController extends Controller {

    public static void startBooking(String body) {

        JsonElement bodyJsonElement = new JsonParser().parse(body);

        JsonArray passengersArray = bodyJsonElement.getAsJsonObject().get("passengers").getAsJsonArray();
        JsonArray cleanPassengersArray= new JsonArray();
        for (int i = 0; i < passengersArray.size(); i++) {
        	String givenName = passengersArray.get(i).getAsJsonObject().get("givenName").getAsString();
        	String surname = passengersArray.get(i).getAsJsonObject().get("surname").getAsString();
        	givenName = cleanString(givenName);
        	surname = cleanString(surname);
        	
        	JsonObject jsonEl = new JsonObject();
        	jsonEl.addProperty("number", passengersArray.get(i).getAsJsonObject().get("number").getAsString());
        	jsonEl.addProperty("passengerType", passengersArray.get(i).getAsJsonObject().get("passengerType").getAsString());
        	jsonEl.addProperty("gender", passengersArray.get(i).getAsJsonObject().get("gender").getAsString());
        	jsonEl.addProperty("givenName", givenName);
        	jsonEl.addProperty("surname", surname);
        	jsonEl.addProperty("foidType", passengersArray.get(i).getAsJsonObject().get("foidType").getAsString());
        	jsonEl.addProperty("foid", passengersArray.get(i).getAsJsonObject().get("foid").getAsString());
        	jsonEl.addProperty("dateOfBirth", passengersArray.get(i).getAsJsonObject().get("dateOfBirth").getAsString());
        	cleanPassengersArray.add(jsonEl);
		}
        
        bodyJsonElement.getAsJsonObject().remove("passengers");
        bodyJsonElement.getAsJsonObject().add("passengers", cleanPassengersArray);
        
        Booking booking = new Booking();

//        Map params = Maps.newHashMap();
//        params.put("id", "_ID_");

//        bodyJsonElement.getAsJsonObject().addProperty("redirect", Router.getFullUrl("PaymentFlowController.processPayment", params));
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
}