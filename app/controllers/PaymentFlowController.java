package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import play.mvc.*;

import utils.AgencyConfigurationDto;
import utils.ApiFlightsSdk.v1.Payment;
import utils.TravelClubUtils;

public class PaymentFlowController extends Controller {

    public static void index() {

        JsonElement bfmResultItem = new JsonParser().parse(params.get("bfmResultItem"));

        AgencyConfigurationDto agencyConfigurationDto = TravelClubUtils.getAgencyConfiguration("56f2d58ce4b0e66b4c0cd92e");

        render(agencyConfigurationDto, bfmResultItem);
    }

    public static void javascript() {
        render("app/views/PaymentFlowController/checkout.js");
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