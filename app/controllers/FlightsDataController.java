package controllers;

import play.mvc.Controller;
import utils.FlightsDataDto;

import java.util.concurrent.TimeUnit;

public class FlightsDataController extends Controller {

    public static void index() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        renderTemplate("FlightsDataController/fligthsData.html");
    }

    public static void priceSuggestionMatrix() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        render();
    }

}