package controllers;

import models.FlightsResultsFilters;
import play.mvc.Controller;
import utils.DateUtils;
import utils.ApiFlightsSdk.v1.BFMSearch;
import utils.ApiFlightsSdk.v1.FlightsAltenateDates;
import utils.dtos.AlternateDatesDto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FlightsDataController extends Controller {

    public static void index() throws InterruptedException {
        BFMSearch bfmSearch  = new BFMSearch();
        bfmSearch.setOrigin(params.get("origin"));
        bfmSearch.setDestination(params.get("destination"));
        bfmSearch.setDeparturedate(DateUtils.reformateDate(params.get("departuredate")));
        bfmSearch.setReturndate(DateUtils.reformateDate(params.get("returndate")));
        bfmSearch.addPassengerType("ADT", params.get("adultcount"));
        bfmSearch.addPassengerType("C02", params.get("childrencount"));
        bfmSearch.addPassengerType("INF", params.get("infantcount"));
        JsonElement flightsResults = bfmSearch.process();

        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(flightsResults);

        renderTemplate("FlightsDataController/flightsData.html",flightsResults, flightsResultsFilters);
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

}