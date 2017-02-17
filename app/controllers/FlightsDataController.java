package controllers;

import java.text.ParseException;

import models.FlightsResultsFilters;
import play.mvc.Controller;
import utils.DateUtils;
import utils.TravelClubUtils;
import utils.ApiFlightsSdk.v1.BFMSearch;
import utils.ApiFlightsSdk.v1.FlightsAltenateDates;
import utils.ApiFlightsSdk.v1.LowFareHistory;
import utils.ApiFlightsSdk.v1.Promotion;
import utils.dtos.AlternateDatesDto;
import utils.dtos.PromotionDto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FlightsDataController extends Controller {

    public static void index() throws InterruptedException {
        PromotionDto promotionDto = new Promotion().getDefault();

        BFMSearch bfmSearch  = new BFMSearch();
        bfmSearch.setOrigin(params.get("origin"));
        bfmSearch.setDestination(params.get("destination"));
        bfmSearch.setCountry(SearchController.getAirportCountry(params.get("destination")));
        bfmSearch.setCity(SearchController.getAirportCityCode(params.get("destination")));
        bfmSearch.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        bfmSearch.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        bfmSearch.addPassengerType("ADT", params.get("adultcount"));
        bfmSearch.addPassengerType("C02", params.get("childrencount"));
        bfmSearch.addPassengerType("INF", params.get("infantcount"));
        bfmSearch.setPromotion(promotionDto.slug);
        
        
        
        
        System.out.println("la ciudad saliente: "+bfmSearch.getCity());
        System.out.println("el pais saliente: "+bfmSearch.getCountry());
        
        JsonElement flightsResults = bfmSearch.process();

        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(flightsResults);

        String dollarExchangeRate = TravelClubUtils.getDollarExchangeRate();
        renderTemplate("FlightsDataController/flightsData.html",flightsResults, flightsResultsFilters, dollarExchangeRate);
    }

    public static void priceSuggestionMatrix() throws InterruptedException {

        FlightsAltenateDates flightsAltenateDates = new FlightsAltenateDates();
        flightsAltenateDates.setOrigin(params.get("origin"));
        flightsAltenateDates.setDestination(params.get("destination"));
        flightsAltenateDates.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        flightsAltenateDates.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        flightsAltenateDates.setPassengercount(params.get("passengercount"));
        JsonElement flightsAltenateDatesResults = flightsAltenateDates.process();

        AlternateDatesDto alternateDatesDto = AlternateDatesDto.parceAlternateDatesDto(flightsAltenateDatesResults);

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
    	renderTemplate("FlightsDataController/airlinesMatrix.html",airlineArray);
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
		}
		
		System.out.println("lowfares wacho: "+lowFaresResults.toString());
        renderTemplate("FlightsDataController/lowFareHistoryMatrix.html",lowFaresArray2);
    }
}