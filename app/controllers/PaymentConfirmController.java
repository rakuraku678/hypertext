package controllers;

import com.google.gson.JsonElement;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Booking;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.TravelClubUtils;
import utils.dtos.PromotionDto;

public class PaymentConfirmController extends Controller {

    public static void index() {
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        Booking booking = new Booking();
        JsonElement bookingResult = booking.get(params.get("id"));

        render(agencyConfigurationDto, bookingResult);
    }

}