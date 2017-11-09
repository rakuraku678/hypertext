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

import java.util.ArrayList;

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

        JsonArray flights = JsonUtils.getJsonArrayFromJson(bookingConfirmationResult.getAsJsonObject(), "flights");
        String airlineCode = "";
        for (JsonElement jsonElement : flights) {
            airlineCode = JsonUtils.getStringFromJson(jsonElement.getAsJsonObject(), "airlineCode");
        }

        JsonArray warnings =  JsonUtils.getJsonArrayFromJson(bookingConfirmationResult.getAsJsonObject(), "warnings");
        if (warnings != null) {
            ArrayList<String> allianceWaningMessages = new ArrayList<String>();
            for (JsonElement warning : warnings) {
                String content = JsonUtils.getStringFromJson(warning.getAsJsonObject(), "content");
                String allianceWarning = Alliance.getAllianceWarningMessage(airlineCode,content);
                if (!allianceWarning.equals("NOT_FFP_WARNING") && !allianceWarning.equals("WARNING_NOT_DEFINED_IN_ALLIANCE")) {
                    allianceWaningMessages.add(allianceWarning);
                }
            }
            if (!allianceWaningMessages.isEmpty()) {
                render(agencyConfigurationDto, bookingConfirmationResult, allianceWaningMessages, promotionDto);
            }
        }
        render(agencyConfigurationDto, bookingConfirmationResult, promotionDto);
    }

}