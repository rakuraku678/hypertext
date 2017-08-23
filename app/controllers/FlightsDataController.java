package controllers;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import models.FlightsResultsFilters;
import play.mvc.Controller;
import play.mvc.Util;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.ApiFlightsSdk.v1.*;
import utils.DateUtils;
import utils.TravelClubUtils;
import utils.dtos.AirportDto;
import utils.dtos.AlternateDatesDto;
import utils.dtos.PromotionDto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FlightsDataController extends Controller {

    public static void index(String slugAgency) throws InterruptedException {
        PromotionDto promotionDto;

        if (!Strings.isNullOrEmpty(params.get("promotion"))) {
            promotionDto = new Promotion().getBySlug(params.get("promotion"));
        } else {
            promotionDto = new Promotion().getDefault();
        }
        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate(promotionDto.agency.externalId);

        BFMSearch bfmSearch  = new BFMSearch();
        bfmSearch.setOrigin(params.get("origin"));
        bfmSearch.setDestination(params.get("destination"));
        AirportDto destinationAirport = new Airport().getByIataCode(params.get("destination"));
        bfmSearch.setCountry(destinationAirport.country);
        bfmSearch.setCity(destinationAirport.iataCityCode);
        bfmSearch.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        bfmSearch.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        bfmSearch.addPassengerType("ADT", params.get("adultcount"));
        bfmSearch.addPassengerType("C02", params.get("childrencount"));
        bfmSearch.addPassengerType("INF", params.get("infantcount"));
        bfmSearch.setCabin(params.get("cabin"));
        bfmSearch.setPromotion(promotionDto.slug);
        bfmSearch.setExternalId(promotionDto.agency.externalId);
        bfmSearch.setDollarExchangeRate(dollarExchangeRate);
        
        JsonElement flightsResults = bfmSearch.process();
        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(flightsResults);

        
        Collection airlineArray = getAirlinePriceArray(flightsResults);

        //renderTemplate("FlightsDataController/flightsData.html", flightsResults, flightsResultsFilters, dollarExchangeRate, airlineArray);

        Template template = TemplateLoader.load(template("FlightsDataController/flightsData.html"));

        Map m = Maps.newHashMap();
        m.put("flightsResults", flightsResults);
        m.put("flightsResultsFilters", flightsResultsFilters);
        m.put("dollarExchangeRate", dollarExchangeRate);
        m.put("airlineArray", airlineArray);
        m.put("params", request.params);

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
        System.out.println(airlinesPrices);
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