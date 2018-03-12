package controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.CheckoutPostDto;
import dto.StateDto;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import utils.AgencyConfigurationDto;
import utils.ConfigurationDto;
import utils.CrossLoginUtils;
import utils.FlightsUtils;
import utils.JsonUtils;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.AirRules;
import utils.ApiFlightsSdk.v1.AirlinesSearch;
import utils.ApiFlightsSdk.v1.Alliance;
import utils.ApiFlightsSdk.v1.Booking;
import utils.ApiFlightsSdk.v1.Country;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AirRulesDto;
import utils.dtos.CountryDto;
import utils.dtos.PromotionDto;

public class PaymentFlowController extends Controller {

    public static void index() {
        PromotionDto promotionDto;
        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        JsonElement bfmResultItem = new JsonParser().parse(params.get("bfmResultItem"));
        JsonObject pricingJsonObject = new JsonParser().parse(params.get("pricingElement")).getAsJsonObject();

        
        JsonElement resultSegmentIda = new JsonParser().parse(params.get("resultSegmentIda"));
        JsonElement resultSegmentVuelta = new JsonParser().parse(params.get("resultSegmentVuelta"));
        
        System.out.println("resultSegmentIda: "+resultSegmentIda.toString());
        System.out.println("==========================");
        System.out.println("resultSegmentVuelta: "+resultSegmentVuelta.toString());
        
        
        
        
        String selectedCurrency = params.get("selectedCurrency");
        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate(promotionDto.agency.externalId);

        JsonObject bfmResultJsonObject = bfmResultItem.getAsJsonObject();
        //JsonObject pricingJsonObject = (JsonObject) JsonUtils.getJsonObjectFromJson(bfmResultJsonObject, "pricing");

        
        List<AirRulesDto> airRulesResultList = Lists.newArrayList();

        AirRules airRules = new AirRules();
        airRules.ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        airRules.travelDate = JsonUtils.getStringFromJson(bfmResultJsonObject,"departureDate");
        airRules.origin = JsonUtils.getStringFromJson(bfmResultJsonObject,"departureAirportCode");
        airRules.destination = JsonUtils.getStringFromJson(bfmResultJsonObject,"returnAirportCode");
        airRules.marketingCarrier = JsonUtils.getStringFromJson(pricingJsonObject,"validatingCarrier");
        if(pricingJsonObject.has("accountCode")){
            airRules.accountCode = JsonUtils.getStringListFromJson(pricingJsonObject,"accountCode").get(0);
        }

        for (String fareBasis : JsonUtils.getStringListFromJson(pricingJsonObject, "fareBasisCodes")) {
            airRules.fareBasis = fareBasis;
            airRulesResultList.add(airRules.process());
        }
        
        Map cityMap = SearchController.getCity(JsonUtils.getStringFromJson(bfmResultJsonObject,"returnCity"));
        boolean onlyPassport = false;
        if (cityMap!=null && cityMap.get("onlyPassport")!=null){
        	onlyPassport = (boolean) cityMap.get("onlyPassport");
        }
        String validatingCarrier = JsonUtils.getStringFromJson(pricingJsonObject,"validatingCarrier");
        String AllianceMessage =  Alliance.getAllianceMessage(validatingCarrier);
        
        Object[] airlineIataCodes =  Alliance.getFFPWhiteList(validatingCarrier).toArray();
        JsonArray whiteListAirlines = new JsonArray();
        if (airlineIataCodes.length>0){
        	whiteListAirlines = AirlinesSearch.process(Arrays.toString(airlineIataCodes).replace("[","").replace("]","")).getAsJsonArray();
        }
        
        List<CountryDto> countriesList = Country.process();
        
        String transactionId = bfmResultItem.getAsJsonObject().get("transactionId").getAsString();
        String token = Cache.get(transactionId, String.class);
        
        StateDto state = null;
        if (!Strings.isNullOrEmpty(token)){
            state = CrossLoginUtils.getState(token);
            System.out.println("state name: "+state.appToken);
            System.out.println("state name: "+state.clientName);
        }
        
        String urlServer = Play.configuration.getProperty("url.server");
        render(agencyConfigurationDto, bfmResultItem, selectedCurrency, dollarExchangeRate, airRulesResultList, promotionDto, 
        		countriesList, onlyPassport, whiteListAirlines, validatingCarrier, AllianceMessage, transactionId, token, state, urlServer, 
        		resultSegmentIda, resultSegmentVuelta);
	
    }

    
    public static void reloadWithTransaction() {
        render("PaymentFlowController/successfulLogin.html");
    }
    
    public static void reloadAfterLogin() {
        render("PaymentFlowController/successfulLogin.html");
    }
    
    public static void processPayment(){

        String promotionSlug = new Booking().promotion(params.get("id"));
        PromotionDto promotionDto = new Promotion().getBySlug(promotionSlug);

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        Map processData = Maps.newHashMap();
        processData.put("checkoutId", params.get("id"));
        processData.put("agency", promotionDto.agency.externalId);
        render(agencyConfigurationDto,processData);
    }

    public static void processEmissionError() {
    	AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(params.get("agency"));
    	String checkoutId = params.get("id");
    	Booking booking = new Booking();
    	String pnr = booking.get(checkoutId).getAsJsonObject().get("pnr").getAsString();
    	render(agencyConfigurationDto, pnr);
    }
    
    public static void processError(String type, String pnr, String a){
    	if (Strings.isNullOrEmpty(pnr)){
    		AgencyConfigurationDto agencyConfigurationDto = null;
    		if (!Strings.isNullOrEmpty(a)){
    			agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(a);
    		}
    		else {
    			PromotionDto promotionDto = new Promotion().getDefault();
    			agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);
    		}

            render(agencyConfigurationDto, type);
    	}
    	else {
        	System.out.println("llega pnr: "+pnr);
        	AgencyConfigurationDto agencyConfigurationDto = null;
    		if (Strings.isNullOrEmpty(a)){
    			agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(a);
    		}
    		else {
    			PromotionDto promotionDto = new Promotion().getDefault();
    			agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);
    		}
            ConfigurationDto configurationDto = FlightsUtils.getConfiguration();
            render(agencyConfigurationDto, type, configurationDto,pnr);
    	}

    }
    
    public static void javascript() {
        render("app/views/PaymentFlowController/checkout.js");
    }

    public static void newPayment(String pnr){
    	if (Strings.isNullOrEmpty(pnr)){
	    	PromotionDto promotionDto;
	        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
	            promotionDto = new Promotion().getBySlug(params.get("promotion"));
	        } else {
	            promotionDto = new Promotion().getDefault();
	        }
	
	        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);



	        render(agencyConfigurationDto);
    	}
    	else {
	    	PromotionDto promotionDto = new Promotion().getDefault();
	        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);
	        ConfigurationDto configurationDto = FlightsUtils.getConfiguration();

            Booking booking = new Booking();
            JsonElement checkout = booking.getByPnr(pnr);

            String groupId = JsonUtils.getStringFromJson(checkout.getAsJsonObject(),"groupId");
            JsonElement flightsBooking = FlightsUtils.getBookingByGroupId(groupId);

            DateTime pendingBookingTime = JsonUtils.getDateTimeFromJson(flightsBooking.getAsJsonObject(),"creationDateTime");
            pendingBookingTime = pendingBookingTime.plusMinutes(Integer.valueOf(configurationDto.maxMinsForPendingBooking));
            Minutes diff = Minutes.minutesBetween(DateTime.now(), pendingBookingTime);
            int mins = diff.getMinutes() >= 0 ? diff.getMinutes(): 0;

            render(configurationDto, agencyConfigurationDto, pnr, mins, checkout);
    	}
    }

    public static void processNewPayment(){
        PromotionDto promotionDto;
        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }

        String pnr = params.get("pnr");
        String surname = params.get("surname");

        if (!Strings.isNullOrEmpty(pnr) && !Strings.isNullOrEmpty(surname)) {
        } else {
            redirect("PaymentFlowController.newPayment");
        }

        Booking booking = new Booking();
        JsonElement retryPaymentResult = booking.retryPayment(pnr,surname);
        JsonObject retryPaymentResultObject = retryPaymentResult.getAsJsonObject();

        String error = JsonUtils.getStringFromJson(retryPaymentResultObject, "error");

        JsonElement data = null;

        switch (error) {
            case "booking_abandon":
                redirect("PaymentFlowController.processError", "abandon");
                break;
            case "booking_failure":
                redirect("PaymentFlowController.processError", "failure");
                break;
            case "booking_canceled":
                redirect("PaymentFlowController.processError", "canceled");
                break;
            case "booking_success":
                data = JsonUtils.getJsonObjectFromJson(retryPaymentResultObject, "data");
                JsonElement checkout = JsonUtils.getJsonObjectFromJson(data.getAsJsonObject(), "checkout");
                String checkoutId = JsonUtils.getStringFromJson(checkout.getAsJsonObject(), "_id");
                //redirect("PaymentConfirmController.index", checkoutId);
                redirect("/checkout/"+checkoutId +"/confirmation");
                break;
            case "surname_validation":
                redirect("PaymentFlowController.processError", "surname_validation");
                break;
            case "pnr_notexists":
                redirect("PaymentFlowController.processError", "pnr_notexists");
                break;
            case "order_pending":
                data = JsonUtils.getJsonObjectFromJson(retryPaymentResultObject, "data");
                String travelPayLocationUrl = JsonUtils.getStringFromJson(data.getAsJsonObject(), "travelPayLocationUrl");
                redirect(travelPayLocationUrl);
                break;
            default:
                redirect("PaymentFlowController.newPayment");
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration(promotionDto.agency.externalId);

        render(agencyConfigurationDto, retryPaymentResultObject);
    }
}
