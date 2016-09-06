package controllers;

import com.google.gson.JsonElement;
import play.mvc.Controller;
import utils.ApiFlightsSdk.v1.FlightsAltenateDates;
import utils.ApiFlightsSdk.v1.FlightsSearch;
import utils.dtos.AlternateDatesDto;

public class FlightsDataController extends Controller {

    public static void index() throws InterruptedException {
        FlightsSearch flightsSearch = new FlightsSearch();
        flightsSearch.setOrigin(params.get("origin"));
        flightsSearch.setDestination(params.get("destination"));
        flightsSearch.setDeparturedate(params.get("departuredate"));
        flightsSearch.setReturndate(params.get("returndate"));
        flightsSearch.setPassengercount(params.get("passengercount"));
        JsonElement flightsResults = flightsSearch.process();

        renderTemplate("FlightsDataController/flightsData.html",flightsResults);
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