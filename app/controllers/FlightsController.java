package controllers;

import com.google.common.base.Strings;

import dto.StateDto;
import play.mvc.Controller;
import utils.AgencyConfigurationDto;
import utils.CrossLoginUtils;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.Agency;
import utils.ApiFlightsSdk.v1.Airport;
import utils.ApiFlightsSdk.v1.CabinConfiguration;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AgencyDto;
import utils.dtos.CabinConfigurationDto;
import utils.dtos.PromotionDto;

public class FlightsController extends Controller {


    public static void d() {
    	String token = CrossLoginUtils.getTransactionToken("56253033e4b0c01cd8c07852","test","state");
    	renderJSON(CrossLoginUtils.getState(token));
    }
    public static void index(String slugAgency) {
        PromotionDto promotionDto;
        String externalId = "";
        CabinConfigurationDto cabinConfigurationDto;
        if (Strings.isNullOrEmpty(slugAgency)){
	        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
	            promotionDto = new Promotion().getBySlug(params.get("promotion"));
	        } else {
	            promotionDto = new Promotion().getDefault();
	        }
	        externalId = promotionDto.agency.externalId;

            cabinConfigurationDto = CabinConfiguration.getByAgency(promotionDto.agency.slug);
        }
        else {
        	AgencyDto agencyDto = new Agency().getBySlug(slugAgency);
        	externalId = agencyDto.externalId;
            cabinConfigurationDto = CabinConfiguration.getByAgency(slugAgency);
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(externalId);
        
        if (!Strings.isNullOrEmpty(params.get("origin"))){
            renderArgs.put("originCity", new Airport().getByIataCode(params.get("origin")).city);
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            renderArgs.put("destinationCity", new Airport().getByIataCode(params.get("destination")).city);
        }
        render(agencyConfigurationDto,cabinConfigurationDto);
    }
    public static void testapitoken(){
    	String token = CrossLoginUtils.getTransactionToken("56253033e4b0c01cd8c07852", "test", "state");
    	StateDto stateDto = CrossLoginUtils.getState(token);

    	renderJSON(stateDto);
    }
}