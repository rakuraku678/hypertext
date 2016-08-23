package controllers;

import com.google.gson.JsonElement;
import play.mvc.Controller;
import utils.ApiFlightsSdk.v1.FlightsSearch;

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
        //TimeUnit.SECONDS.sleep(5);
        render();
    }

}