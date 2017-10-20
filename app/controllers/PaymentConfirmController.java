package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.mvc.Controller;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Alliance;
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


        JsonArray allianceWaningMessages = new JsonArray();
        JsonArray warnings =  JsonUtils.getJsonArrayFromJson(bookingConfirmationResult.getAsJsonObject(), "warnings");


        JsonObject order = (JsonObject) JsonUtils.getJsonObjectFromJson(bookingConfirmationResult.getAsJsonObject(), "order");
        JsonArray flights = JsonUtils.getJsonArrayFromJson(bookingConfirmationResult.getAsJsonObject(), "flights");

        String airlineCode = "";
        for (JsonElement jsonElement : flights) {
            airlineCode = JsonUtils.getStringFromJson(jsonElement.getAsJsonObject(), "airlineCode");
        }

        if (warnings != null) {
            for (JsonElement warning : warnings) {
                JsonElement allianceErrorResponse = Alliance.getAllianceWarningMessage(airlineCode,
                        JsonUtils.getStringFromJson(
                                warning.getAsJsonObject(), "content"));
                if (!allianceErrorResponse.getAsString().equals("NOT_FFP_WARNING")) {
                    allianceWaningMessages.add(allianceErrorResponse);
                }
            }
        }
        String orderStatus = JsonUtils.getStringFromJson(order,"status");
        if(!orderStatus.equals("success")){
            PaymentFlowController.processError("order-" + orderStatus);
        }
        if (allianceWaningMessages.size() != 0 ) {
            render(agencyConfigurationDto, bookingConfirmationResult, allianceWaningMessages);
        }
        render(agencyConfigurationDto, bookingConfirmationResult);
    }

}