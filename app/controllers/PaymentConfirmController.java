package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.TravelClubUtils;

public class PaymentConfirmController extends Controller {

    public static void index() {
        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration("56f2d58ce4b0e66b4c0cd92e");

        render(agencyConfigurationDto);
    }

}