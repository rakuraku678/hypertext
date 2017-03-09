package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Booking;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.JsonUtils;
import utils.TravelClubUtils;
import utils.dtos.PromotionDto;

public class PaymentConfirmController extends Controller {

    public static void index() {
        String promotionSlug = null;
        PromotionDto promotionDto;

        try {
            promotionSlug = new Booking().promotion(params.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (promotionSlug == null) {
            promotionDto = new Promotion().getDefault();
        } else {
            promotionDto = new Promotion().getBySlug(promotionSlug);
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        Booking booking = new Booking();
        JsonElement bookingConfirmationResult = booking.confirmation(params.get("id"));

        JsonObject order = (JsonObject) JsonUtils.getJsonObjectFromJson(bookingConfirmationResult.getAsJsonObject(), "order");
        String orderStatus = JsonUtils.getStringFromJson(order,"status");
        if(!orderStatus.equals("success")){
            PaymentFlowController.processError("order-" + orderStatus);
        }

        render(agencyConfigurationDto, bookingConfirmationResult);
    }

}