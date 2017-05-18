package controllers;

import com.google.common.base.Strings;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Airport;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.TravelClubUtils;
import utils.dtos.PromotionDto;

public class FlightsController extends Controller {

    public static void index() {
        PromotionDto promotionDto;

        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);


        if (!Strings.isNullOrEmpty(params.get("origin"))){
            renderArgs.put("originCity", new Airport().getByIataCode(params.get("origin")).city);
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            renderArgs.put("destinationCity", new Airport().getByIataCode(params.get("destination")).city);
        }

        render(agencyConfigurationDto);
    }

}