package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.Controller;

public class BookingController extends Controller {

    public static void startBooking(String body) {
        JsonElement bodyJsonElement = new JsonParser().parse(body);
        renderJSON(bodyJsonElement);
    }

}