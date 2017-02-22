package controllers;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import play.mvc.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Payment;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.TravelClubUtils;
import utils.dtos.PromotionDto;

import java.util.Map;

public class PaymentFlowController extends Controller {

    public static void index() {
    	
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        JsonElement bfmResultItem = new JsonParser().parse(params.get("bfmResultItem"));

        
        System.out.println("a ver que hay aca: "+bfmResultItem.toString());
        String selectedCurrency = params.get("selectedCurrency");
        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate();
        
        render(agencyConfigurationDto, bfmResultItem, selectedCurrency, dollarExchangeRate);
    }

    public static void processPayment(){
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        Map processData = Maps.newHashMap();
        processData.put("checkoutId", params.get("id"));

        render(agencyConfigurationDto,processData);
    }

    public static void processError(String type){
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        render(agencyConfigurationDto, type);
    }

    public static void javascript() {
        render("app/views/PaymentFlowController/checkout.js");
    }
}