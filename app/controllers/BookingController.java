package controllers;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.Controller;
import play.mvc.Router;
import utils.ApiFlightsSdk.v1.Booking;

import java.util.Map;

public class BookingController extends Controller {

    public static void startBooking(String body) {
        JsonElement bodyJsonElement = new JsonParser().parse(body);

        Booking booking = new Booking();

        Map params = Maps.newHashMap();
        params.put("id", "_ID_");

        bodyJsonElement.getAsJsonObject().addProperty("redirect",  Router.getFullUrl("PaymentFlowController.processPayment", params));
        bodyJsonElement.getAsJsonObject().addProperty("agencyId", "55195834e4b0f0cd8b323427");
        renderJSON(booking.process(bodyJsonElement));
    }

    public static void statusBooking() {
        Booking booking = new Booking();
        renderJSON(booking.statusControl(params.get("id")));
    }

}