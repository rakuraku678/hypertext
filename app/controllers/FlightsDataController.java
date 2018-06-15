package controllers;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.StateDto;
import models.FlightsResultsFilters;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Util;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.CrossLoginUtils;
import utils.DateUtils;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.Airport;
import utils.ApiFlightsSdk.v1.BFMSearch;
import utils.ApiFlightsSdk.v1.FlightsAltenateDates;
import utils.ApiFlightsSdk.v1.LowFareHistory;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AirportDto;
import utils.dtos.AlternateDatesDto;
import utils.dtos.PromotionDto;

public class FlightsDataController extends Controller {

	
    public static void index(String slugAgency) throws InterruptedException {
        PromotionDto promotionDto;

        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }
        String dollarExchangeRate = "630";//TravelClubUtils.getDollarExchangeRate(promotionDto.agency.externalId);
        String transactionId = params.get("transactionId");
        
        BFMSearch bfmSearch  = new BFMSearch();
        bfmSearch.setOrigin(params.get("origin"));
        bfmSearch.setDestination(params.get("destination"));
        AirportDto destinationAirport = new Airport().getByIataCode(params.get("destination"));
        AirportDto departureAirport = new Airport().getByIataCode(params.get("origin"));
        bfmSearch.setCountry(destinationAirport.country);
        bfmSearch.setCity(destinationAirport.iataCityCode);
        bfmSearch.setDepartureCountry(departureAirport.country);
        bfmSearch.setDepartureCity(departureAirport.iataCityCode);
        bfmSearch.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        bfmSearch.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        bfmSearch.addPassengerType("ADT", params.get("adultcount"));
        bfmSearch.addPassengerType("C02", params.get("childrencount"));
        bfmSearch.addPassengerType("INF", params.get("infantcount"));
        bfmSearch.setCabin(params.get("cabin"));
        bfmSearch.setPromotion(promotionDto.slug);
        bfmSearch.setExternalId(promotionDto.agency.externalId);
        bfmSearch.setDollarExchangeRate(dollarExchangeRate);
        if (!Strings.isNullOrEmpty(transactionId)){
        	bfmSearch.setTransactionId(transactionId);
        }
        JsonElement flightsResults = bfmSearch.process();
        
        //TODO: tener en cuenta en el display de filtros el nuevo objeto BigResult
        JsonElement smallResults = flightsResults.getAsJsonArray().get(0).getAsJsonObject().get("airSearchResults");
        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(smallResults);

        //TODO: tener en cuenta en el display de aerolineas el nuevo objeto BigResult
        Map<String,String> allStops = getAllStops(flightsResults);

        Collection airlineArray = getAirlinePriceArray(smallResults);
        
        if (Strings.isNullOrEmpty(transactionId)){
            //TODO: tener en cuenta en la obtencion de transactionId el nuevo objeto BigResult
        	transactionId = smallResults.getAsJsonArray().get(0).getAsJsonObject().get("transactionId").getAsString();
        }
        String tknumber = params.get("tknumber");
        System.out.println("TKNUMBER: "+tknumber);
        String token = Cache.get(transactionId, String.class);
        System.out.println("transactionId: "+transactionId+", token: "+token);
        
        StateDto state = null;
        if (!Strings.isNullOrEmpty(token)){
            state = CrossLoginUtils.getState(token);
            state.transactionId = transactionId;
            System.out.println("state name: "+state.appToken);
            System.out.println("state name: "+state.clientName);
        }
        
        Template template = TemplateLoader.load(template("FlightsDataController/flightsData.html"));
        Map m = Maps.newHashMap();
        m.put("flightsResults", smallResults);
        m.put("bigFlightsResults", flightsResults);
        m.put("flightsResultsFilters", flightsResultsFilters);
        m.put("dollarExchangeRate", dollarExchangeRate);
        m.put("airlineArray", airlineArray);
        m.put("params", request.params);
        m.put("transactionId", transactionId);
        m.put("state", state);
        m.put("promotionDto", promotionDto);
        m.put("tknumber", tknumber);
        m.put("allStops", allStops);
        
        renderHtml(template.render(m).replaceAll("\\s{2,}"," "));
    }
    
    private static Map<String,String> getAllStops(JsonElement flightsResults) {
    	Map<String,String> ticketsAndStops = Maps.newHashMap();
    	JsonArray flightsArray = flightsResults.getAsJsonArray();
    	int i = 1;
    	for (JsonElement flight : flightsArray) {
    		JsonArray segmentArray = flight.getAsJsonObject().get("idaAirSearchSegments").getAsJsonArray();
    		
    		StringBuilder airlineBuilder = new StringBuilder();
    		for (JsonElement segmentElement : segmentArray) {
    			String airlineName = segmentElement.getAsJsonObject().get("detail").getAsJsonArray().get(0).getAsJsonObject().get("marketingAirline").getAsString();
				Integer stops = segmentElement.getAsJsonObject().get("flightsCount").getAsInt()-1;
				if (airlineBuilder.indexOf(airlineName+"-"+stops.toString())==-1){
					airlineBuilder.append(airlineName+"-"+stops.toString()+",");
				}
			}
    		ticketsAndStops.put("I_"+i, airlineBuilder.substring(0,airlineBuilder.length()-1).toString());
    		
    		airlineBuilder = new StringBuilder();
    		segmentArray = flight.getAsJsonObject().get("vueltaAirSearchSegments").getAsJsonArray();
    		
    		for (JsonElement segmentElement : segmentArray) {
    			String airlineName = segmentElement.getAsJsonObject().get("detail").getAsJsonArray().get(0).getAsJsonObject().get("marketingAirline").getAsString();
				Integer stops = segmentElement.getAsJsonObject().get("flightsCount").getAsInt()-1;
				
				if (airlineBuilder.indexOf(airlineName+"-"+stops.toString())==-1){
					airlineBuilder.append(airlineName+"-"+stops.toString()+",");
				}
			}
    		ticketsAndStops.put("V_"+i, airlineBuilder.substring(0,airlineBuilder.length()-1).toString());
    		i++;
    	}
    	
    	
		return ticketsAndStops;
	}

	private static Map<String,List<String>> getAirlineBigPriceArray(JsonElement bigFlightResults) {
		
		return null;
	}

	public static void indexDeprecated(String slugAgency) throws InterruptedException {
        PromotionDto promotionDto;

        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }
        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate(promotionDto.agency.externalId);
        String transactionId = params.get("transactionId");
        
        BFMSearch bfmSearch  = new BFMSearch();
        bfmSearch.setOrigin(params.get("origin"));
        bfmSearch.setDestination(params.get("destination"));
        AirportDto destinationAirport = new Airport().getByIataCode(params.get("destination"));
        AirportDto departureAirport = new Airport().getByIataCode(params.get("origin"));
        bfmSearch.setCountry(destinationAirport.country);
        bfmSearch.setCity(destinationAirport.iataCityCode);
        bfmSearch.setDepartureCountry(departureAirport.country);
        bfmSearch.setDepartureCity(departureAirport.iataCityCode);
        bfmSearch.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        bfmSearch.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        bfmSearch.addPassengerType("ADT", params.get("adultcount"));
        bfmSearch.addPassengerType("C02", params.get("childrencount"));
        bfmSearch.addPassengerType("INF", params.get("infantcount"));
        bfmSearch.setCabin(params.get("cabin"));
        bfmSearch.setPromotion(promotionDto.slug);
        bfmSearch.setExternalId(promotionDto.agency.externalId);
        bfmSearch.setDollarExchangeRate(dollarExchangeRate);
        if (!Strings.isNullOrEmpty(transactionId)){
        	bfmSearch.setTransactionId(transactionId);
        }
        JsonElement flightsResults = bfmSearch.process();
        
        //TODO: tener en cuenta en el display de filtros el nuevo objeto BigResult
        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(flightsResults);

        //TODO: tener en cuenta en el display de aerolineas el nuevo objeto BigResult
        Collection airlineArray = getAirlinePriceArray(flightsResults);

        if (Strings.isNullOrEmpty(transactionId)){
            //TODO: tener en cuenta en la obtencion de transactionId el nuevo objeto BigResult
        	transactionId = flightsResults.getAsJsonArray().get(0).getAsJsonObject().get("transactionId").getAsString();
        }
        String tknumber = params.get("tknumber");
        System.out.println("TKNUMBER: "+tknumber);
        String token = Cache.get(transactionId, String.class);
        System.out.println("transactionId: "+transactionId+", token: "+token);
        
        StateDto state = null;
        if (!Strings.isNullOrEmpty(token)){
            state = CrossLoginUtils.getState(token);
            state.transactionId = transactionId;
            System.out.println("state name: "+state.appToken);
            System.out.println("state name: "+state.clientName);
        }
        
        Template template = TemplateLoader.load(template("FlightsDataController/flightsData.html"));
        Map m = Maps.newHashMap();
        m.put("flightsResults", flightsResults);
        m.put("flightsResultsFilters", flightsResultsFilters);
        m.put("dollarExchangeRate", dollarExchangeRate);
        m.put("airlineArray", airlineArray);
        m.put("params", request.params);
        m.put("transactionId", transactionId);
        m.put("state", state);
        m.put("promotionDto", promotionDto);
        m.put("tknumber", tknumber);
        
        renderHtml(template.render(m).replaceAll("\\s{2,}"," "));
    }

    @Util
    private static Collection getAirlinePriceArray(JsonElement flightsResults) {
        Map<String, Map<String, Object>> airlineMap = Maps.newHashMap();
        for (JsonElement json : flightsResults.getAsJsonArray()) {
            String airlineCode = json.getAsJsonObject().get("pricing").getAsJsonObject().get("validatingCarrier").getAsString();
            int layoverCountDeparture = json.getAsJsonObject().get("departureSegment").getAsJsonObject().get("flightsCount").getAsInt() - 1;
            int layoverCountReturn = json.getAsJsonObject().get("returnSegment").getAsJsonObject().get("flightsCount").getAsInt() - 1;
            int layoverCount = Math.min(2, Math.max(layoverCountDeparture, layoverCountReturn));
            double price = json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPrice").getAsDouble();
            
            if ((json.getAsJsonObject().get("pricingCLP").getAsJsonObject().get("adtTotalPrice").getAsDouble() > json.getAsJsonObject().get("pricingCLP").getAsJsonObject().get("adtTotalPrice").getAsDouble() 
            		&& json.getAsJsonObject().get("pricing").getAsJsonObject().has("adtTotalPriceBeforeDiscount") && 
            		json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPriceBeforeDiscount").getAsDouble() != json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPrice").getAsDouble() )
             ||(json.getAsJsonObject().get("pricingCLP").getAsJsonObject().get("adtTotalPriceWithAllTaxes").getAsDouble() <= json.getAsJsonObject().get("pricingCLP").getAsJsonObject().get("adtTotalPrice").getAsDouble() 
            		&& json.getAsJsonObject().get("pricing").getAsJsonObject().has("adtTotalPriceBeforeDiscount") && 
            		json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPriceBeforeDiscount").getAsDouble() != json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPrice").getAsDouble()
            		) ){																												
            	price = Double.valueOf(json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPriceString").getAsString());
            }
            else {
            	price = Double.valueOf(json.getAsJsonObject().get("pricing").getAsJsonObject().get("adtTotalPriceWithAllTaxes").getAsString().replaceAll(",", "."));
            }
            
            String key = airlineCode;

            if (airlineMap.get(key) == null){
                Map m = Maps.newHashMap();
                m.put("airline", airlineCode);
                m.put("price" + layoverCount, price);
                airlineMap.put(key, m);
            }
            else {
                Map m = airlineMap.get(key);
                if (m.get("price" + layoverCount) != null) {
                    double p = (double) m.get("price" + layoverCount);
                    if (p < price) {
                        m.put("price" + layoverCount, p);
                    }
                }
                else {
                    m.put("price" + layoverCount, price);
                }
            }
        }
        return airlineMap.values();
    }

    public static void priceSuggestionMatrix() throws InterruptedException {
        PromotionDto promotionDto;
        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }

        FlightsAltenateDates flightsAltenateDates = new FlightsAltenateDates();
        flightsAltenateDates.setOrigin(params.get("origin"));
        flightsAltenateDates.setDestination(params.get("destination"));
        flightsAltenateDates.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        flightsAltenateDates.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        flightsAltenateDates.addPassengerType("ADT", params.get("adultcount"));
        flightsAltenateDates.addPassengerType("C02", params.get("childrencount"));
        flightsAltenateDates.addPassengerType("INF", params.get("infantcount"));
        flightsAltenateDates.setPromotion(promotionDto.slug);
        flightsAltenateDates.setCabin(params.get("cabin"));
        JsonElement flightsAltenateDatesResults = flightsAltenateDates.process();

        AlternateDatesDto alternateDatesDto = AlternateDatesDto.parseAlternateDatesDto(flightsAltenateDatesResults);

        alternateDatesDto.detailAlternateDatesDtoList.sort((o1, o2) -> {
            int cmp = o1.returnDate.compareTo(o2.returnDate);
            if (cmp == 0)
                cmp = o1.departureDate.compareTo(o2.departureDate);
            return cmp;
        });

        renderTemplate("FlightsDataController/priceSuggestionMatrix.html",alternateDatesDto);
    }
    
    public static void priceAirlinesMatrix(String airlinesPrices, String carriersNames){
        JsonArray airlineArray = new JsonParser().parse(airlinesPrices).getAsJsonArray();
        renderTemplate("FlightsDataController/airlinesMatrix.html", airlineArray);
    }
    
    public static void lowPricesMatrix(String origin, String destination, String departureDate, String returnDate) throws ParseException{

		LowFareHistory lowFareHistory = new LowFareHistory();
		lowFareHistory.setOrigin(params.get("origin"));
		lowFareHistory.setDestination(params.get("destination"));
		lowFareHistory.setDeparturedate(DateUtils.reformateDate(departureDate));
		lowFareHistory.setReturndate(DateUtils.reformateDate(returnDate));
		
		JsonElement lowFaresResults = lowFareHistory.process();
		JsonArray lowFaresArray = lowFaresResults.getAsJsonArray();
		
		JsonArray lowFaresArray2 = new JsonArray();
		for (int i = 0; i < lowFaresArray.size(); i++) {
			if (lowFaresArray.get(i).getAsJsonObject().get("fare").getAsString().equals("N/A")){
				JsonObject jsonEl = new JsonObject();
				jsonEl.addProperty("fare", "0");
				jsonEl.addProperty("shopDate", lowFaresArray.get(i).getAsJsonObject().get("shopDate").getAsString());
				lowFaresArray2.add(jsonEl);
			}
			else {
				JsonObject jsonEl = new JsonObject();
				jsonEl.addProperty("fare", lowFaresArray.get(i).getAsJsonObject().get("fare").getAsString());
				jsonEl.addProperty("shopDate", lowFaresArray.get(i).getAsJsonObject().get("shopDate").getAsString());
				lowFaresArray2.add(jsonEl);
			}
		}
		
		System.out.println("lowfares wacho: "+lowFaresResults.toString());
        renderTemplate("FlightsDataController/lowFareHistoryMatrix.html",lowFaresArray2);
    }
}