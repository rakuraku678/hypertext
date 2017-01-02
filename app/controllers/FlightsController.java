package controllers;

import com.google.common.base.Strings;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.TravelClubUtils;

public class FlightsController extends Controller {

    public static void index() {
        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration("576d8872558bc20506a72392");

        if (!Strings.isNullOrEmpty(params.get("origin"))){
            Map m = (Map) SearchController.getCachedAutoComplete(params.get("origin")).get(0);
            renderArgs.put("originCity", m.get("city"));
        }

        if (!Strings.isNullOrEmpty(params.get("destination"))){
            Map m = (Map) SearchController.getCachedAutoComplete(params.get("destination")).get(0);
            renderArgs.put("destinationCity", m.get("city"));
        }

        render(agencyConfigurationDto);
    }

}