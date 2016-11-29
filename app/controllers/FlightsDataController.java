package controllers;

import models.FlightsResultsFilters;
import play.mvc.Controller;
import utils.ApiFlightsSdk.v1.FlightsAltenateDates;
import utils.ApiFlightsSdk.v1.FlightsSearch;
import utils.dtos.AlternateDatesDto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FlightsDataController extends Controller {

    public static void index() throws InterruptedException {
        FlightsSearch flightsSearch = new FlightsSearch();
        flightsSearch.setOrigin(params.get("origin"));
        flightsSearch.setDestination(params.get("destination"));
        flightsSearch.setDeparturedate(params.get("departuredate"));
        flightsSearch.setReturndate(params.get("returndate"));
        flightsSearch.setPassengercount(params.get("passengercount"));
        flightsSearch.setOutboundflightstops(params.get("outboundflightstops"));
        flightsSearch.setIncludedcarriers(params.get("includedcarriers"));
        flightsSearch.setOutbounddeparturewindow(params.get("outbounddeparturewindow"));
        flightsSearch.setInboundarrivalwindow(params.get("inboundarrivalwindow"));
        JsonElement flightsResults = flightsSearch.process();

        
   
        FlightsResultsFilters flightsResultsFilters = FlightsResultsFilters.processFlightsResults(flightsResults);


        renderTemplate("FlightsDataController/flightsData.html",flightsResults, flightsResultsFilters);
    }

    public static void priceSuggestionMatrix() throws InterruptedException {

        FlightsAltenateDates flightsAltenateDates = new FlightsAltenateDates();
        flightsAltenateDates.setOrigin(params.get("origin"));
        flightsAltenateDates.setDestination(params.get("destination"));
        flightsAltenateDates.setDeparturedate(params.get("departuredate"));
        flightsAltenateDates.setReturndate(params.get("returndate"));
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

}