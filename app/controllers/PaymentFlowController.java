package controllers;

import dto.CheckoutPostDto;
import play.*;
import play.mvc.*;

import java.lang.reflect.Field;
import java.util.*;

import models.*;
import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Payment;
import utils.TravelClubUtils;

public class PaymentFlowController extends Controller {

    public static void index() {
        CheckoutPostDto checkoutPostDto = new CheckoutPostDto();

        for (Field f : CheckoutPostDto.class.getDeclaredFields()) {
            checkoutPostDto.set(f.getName(),params.get(f.getName()));
        }

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration("56f2d58ce4b0e66b4c0cd92e");

        render(agencyConfigurationDto, checkoutPostDto);
    }

    public static void callback(){
        renderText("CALLBACK");
    }

    public static void startPayment(){
        Router.ActionDefinition redirectAction = Router.reverse("PaymentConfirmController.index");
        Router.ActionDefinition callbackAction = Router.reverse("PaymentFlowController.callback");
        redirectAction.absolute();
        callbackAction.absolute();

        Payment payment = new Payment();

        payment.external_id = "internal test vuelos v3";
        payment.agency_id = "56f2d58ce4b0e66b4c0cd92e";
        payment.redirection_url = redirectAction.url;
        payment.callback_url = callbackAction.url;
        payment.currency = "clp";
        payment.amount = params.get("totalFare");
        payment.tax = params.get("totalFare");
        payment.subject = "Subject";
        payment.client_name = "cliente name";

        String location = payment.process();

        redirect(location);
    }

}