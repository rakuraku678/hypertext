package controllers;

import com.google.common.base.Strings;

import dto.StateDto;
import play.cache.Cache;
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


    public static void index(String slugAgency) {
        PromotionDto promotionDto = null;
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
            promotionDto = new PromotionDto();
            promotionDto.agency = agencyDto; 
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(externalId);
        
        if (!Strings.isNullOrEmpty(params.get("origin"))){
            renderArgs.put("originCity", new Airport().getByIataCode(params.get("origin")).city);
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            renderArgs.put("destinationCity", new Airport().getByIataCode(params.get("destination")).city);
        }
        
        if (promotionDto==null){
        	System.out.println("es nulo");
        }
        
        String transactionId = "0";
        String token = Cache.get(transactionId, String.class);
        
        StateDto state = null;
        if (!Strings.isNullOrEmpty(token)){
            state = CrossLoginUtils.getState(token);
            System.out.println("state name: "+state.appToken);
            System.out.println("state name: "+state.clientName);
        }

        render(agencyConfigurationDto, cabinConfigurationDto, promotionDto);
    }
    public static void reloadWithTransaction() {
        render("PaymentFlowController/successfulLogin.html");
    }
    
}