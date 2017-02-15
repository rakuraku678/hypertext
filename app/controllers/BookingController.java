package controllers;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.Controller;
import play.mvc.Router;
import utils.ApiFlightsSdk.v1.Booking;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.PromotionDto;

import java.util.Map;

public class BookingController extends Controller {

    public static void startBooking(String body) {
        PromotionDto promotionDto = new Promotion().getDefault();

        JsonElement bodyJsonElement = new JsonParser().parse(body);

        Booking booking = new Booking();

        Map params = Maps.newHashMap();
        params.put("id", "_ID_");

        bodyJsonElement.getAsJsonObject().addProperty("redirect", Router.getFullUrl("PaymentFlowController.processPayment", params));
        bodyJsonElement.getAsJsonObject().addProperty("agencyId", promotionDto.agency.externalId);

        renderJSON(booking.process(bodyJsonElement));
    }

    public static void statusBooking() {
        Booking booking = new Booking();
        renderJSON(booking.statusControl(params.get("id")));
    }

}