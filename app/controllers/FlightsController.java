package controllers;

import com.google.common.base.Strings;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.TravelClubUtils;
import utils.dtos.PromotionDto;

public class FlightsController extends Controller {

    public static void index() {
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);


        if (!Strings.isNullOrEmpty(params.get("origin"))){
            Map m = (Map) SearchController.getCachedAutoComplete(params.get("origin")).get(0);
            renderArgs.put("originCity", m.get("city"));
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            Map m = (Map) SearchController.getCachedAutoComplete(params.get("destination")).get(0);
            renderArgs.put("destinationCity", m.get("city"));
        }

        render(agencyConfigurationDto);
    }

}