package controllers;

import com.google.common.base.Strings;

import play.mvc.Controller;
import utils.AgencyConfigurationDto;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.Agency;
import utils.ApiFlightsSdk.v1.Airport;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AgencyDto;
import utils.dtos.PromotionDto;

public class FlightsController extends Controller {

    public static void index(String slugAgency) {
        PromotionDto promotionDto;
        String externalId = "";
        if (Strings.isNullOrEmpty(slugAgency)){
	        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
	            promotionDto = new Promotion().getBySlug(params.get("promotion"));
	        } else {
	            promotionDto = new Promotion().getDefault();
	        }
	        externalId = promotionDto.agency.externalId;
        }
        else {
        	AgencyDto agencyDto = new Agency().getBySlug(slugAgency);
        	externalId = agencyDto.externalId;
        }
        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(externalId);

        if (!Strings.isNullOrEmpty(params.get("origin"))){
            renderArgs.put("originCity", new Airport().getByIataCode(params.get("origin")).city);
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            renderArgs.put("destinationCity", new Airport().getByIataCode(params.get("destination")).city);
        }

        render(agencyConfigurationDto);
    }

}