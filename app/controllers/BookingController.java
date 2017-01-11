package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.Controller;
import utils.ApiFlightsSdk.v1.Booking;

public class BookingController extends Controller {

    public static void startBooking(String body) {
        JsonElement bodyJsonElement = new JsonParser().parse(body);

        Booking booking = new Booking();
        renderJSON(booking.process(bodyJsonElement));
    }

}