package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.TravelClubUtils;

public class FlightsController extends Controller {

    public static void index() {
        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration("576d8872558bc20506a72392");

        render(agencyConfigurationDto);
    }

}