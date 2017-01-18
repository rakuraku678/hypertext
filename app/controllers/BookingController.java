package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.Controller;
import play.mvc.Router;
import utils.ApiFlightsSdk.v1.Booking;

public class BookingController extends Controller {

    public static void startBooking(String body) {
        JsonElement bodyJsonElement = new JsonParser().parse(body);

        Booking booking = new Booking();

        Router.ActionDefinition redirectAction = Router.reverse("PaymentFlowController.processPayment");

        String secure = "";
        if(request.secure){
            secure = "https://";
        } else {
            secure = "http://";
        }

        bodyJsonElement.getAsJsonObject().addProperty("redirect", secure + request.host + redirectAction.url);
        renderJSON(booking.process(bodyJsonElement));
    }

}