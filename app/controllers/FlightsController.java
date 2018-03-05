package controllers;

import com.google.common.base.Strings;

import play.mvc.Controller;
import utils.AgencyConfigurationDto;
import utils.SearchConfigurationDto;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.Agency;
import utils.ApiFlightsSdk.v1.Airport;
import utils.ApiFlightsSdk.v1.CabinConfiguration;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AgencyDto;
import utils.dtos.CabinConfigurationDto;
import utils.dtos.PromotionDto;

public class FlightsController extends Controller {


    public static void index(String slugAgency, String transactionId, String tknumber) {
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

        System.out.println("tknumber: "+tknumber);
        
        SearchConfigurationDto searchConfigurationDto = SearchConfigurationDto.getSearchConfiguration();
        
        render(agencyConfigurationDto, cabinConfigurationDto, promotionDto, transactionId, tknumber, searchConfigurationDto);
    }
    public static void reloadWithTransaction(String transactionId,String tknumber) {
        render("FlightsController/successfulLogin.html",transactionId, tknumber);
    }
}