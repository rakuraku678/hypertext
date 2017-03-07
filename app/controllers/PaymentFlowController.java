package controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import play.mvc.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.AirRules;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.JsonUtils;
import utils.TravelClubUtils;
import utils.dtos.AirRulesDto;
import utils.dtos.PromotionDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PaymentFlowController extends Controller {

    public static void index() {
    	
        PromotionDto promotionDto = new Promotion().getDefault();

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        JsonElement bfmResultItem = new JsonParser().parse(params.get("bfmResultItem"));

        
        System.out.println("a ver que hay aca: "+bfmResultItem.toString());
        String selectedCurrency = params.get("selectedCurrency");
        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate();

        JsonObject bfmResultJsonObject = bfmResultItem.getAsJsonObject();
        JsonObject pricingJsonObject = (JsonObject) JsonUtils.getJsonObjectFromJson(bfmResultJsonObject, "pricing");

        List<AirRulesDto> airRulesResultList = Lists.newArrayList();

        AirRules airRules = new AirRules();
        airRules.ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        airRules.travelDate = JsonUtils.getStringFromJson(bfmResultJsonObject,"departureDate");
        airRules.origin = JsonUtils.getStringFromJson(bfmResultJsonObject,"departureAirportCode");
        airRules.destination = JsonUtils.getStringFromJson(bfmResultJsonObject,"returnAirportCode");
        airRules.marketingCarrier = JsonUtils.getStringFromJson(pricingJsonObject,"validatingCarrier");
        for (JsonElement jsonElement : JsonUtils.getJsonArrayFromJson(pricingJsonObject, "fareBasisCodes")) {
            airRules.fareBasis = jsonElement.getAsString();
            airRulesResultList.add(airRules.process());
        }
        render(agencyConfigurationDto, bfmResultItem, selectedCurrency, dollarExchangeRate, airRulesResultList);
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