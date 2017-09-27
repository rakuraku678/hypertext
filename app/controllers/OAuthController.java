package controllers;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import utils.AESEncryptorUtil;

public class OAuthController extends Controller {

	
	private static final String OAUTH_SERVER_URL = "http://190.98.204.155:7001/AuthServer/oauth/authorize";//https://200.14.140.84:4443/AuthServer/oauth/authorize
	private static final String OAUTH_SERVER_URL_PROD = "https://servicios.bancochile.cl/AuthServer/authorization";
    private static final String AES_KEY = "Bar12345Bar12345";

    
    public static void renderBancoChileLogin(String hash) {
System.out.println("el hash que llega es: "+hash);
        try {
            String state = hash+";"+"travel_club";

            state = AESEncryptorUtil.encrypt(state, AES_KEY);

            String clientId = "travel_club";

            String callbackUrl = "CALLBACK URL";
            String travelClubLoginUrl = OAUTH_SERVER_URL + "?response_type=code&state=" + state + "&client_id=" + clientId + "&scope=read+write+delete";

            Logger.info("Url de login a banco: " + travelClubLoginUrl);
            render("/PaymentFlowController/redirectToLogin.html", travelClubLoginUrl, callbackUrl);


        } catch (Exception ex){
        	ex.printStackTrace();
        }
    }

//    public static void verifyBancoChileLogin(String code, String state) {
//
//        CheckoutOrder order = null;
//        String orderHash = null;
//
//        Logger.info("Datos recibidos de bch luego del login:");
//        Logger.info("code: " + code);
//        Logger.info("state: " + state);
//
//        try {
//
//            Logger.info("Se desencriptda orderHash...");
//
//            String stateDecrypted = AESEncryptorUtil.decrypt(state, AES_KEY);
//            orderHash = stateDecrypted.split(";")[0];
//
//            Logger.info("orderHash: " + orderHash);
//
//            Logger.info("Se verificará el login...");
//
//            BchLoginForCheckout bchLoginForCheckout = new BchLoginForCheckout(code, state, orderHash);
//
//            order = bchLoginForCheckout.getOrder();
//
//            bchLoginForCheckout.doLoginConfirmation();
//
//            if (bchLoginForCheckout.loginWasSuccessful()) {
//
//                Logger.info("Login exitoso...");
//
//                bchLoginForCheckout.loadRutInOrder();
//
//                Agency agency = order.agency;
//
//                Logger.info("Mostrar página de sumario: " + agency.showCheckoutSummaryStep);
//
//                if (agency.showCheckoutSummaryStep) {
//                    Logger.info("Se renderea sumario...");
//                    String template = agency.getTemplate();
//                    render("/templates/" + template + "/checkout/checkout.html", agency, template, order, orderHash);
//                } else {
//                    Logger.info("Se renderea pasarela de pago...");
//                    CheckoutController.paymentInformation("bch", orderHash, null);
//                }
//
//            } else {
//
//                Agency agency = order.agency;
//                String template = agency.getTemplate();
//                String errorMsg = "No se pudo procesar su compra o canje. No tiene permisos para ejecutar esta operación.";
//                String hash = orderHash;
//
//                render("/templates/" + template + "/checkout/error.html", errorMsg, agency, template, order, hash);
//            }
//        } catch ( Exception e){
//
//            Logger.info("Se produjo una excepción al momento de mostrar la orden. Mensaje: " + e.getMessage());
//            e.printStackTrace();
//
//            if (order!=null){
//                Agency agency = order.agency;
//                String template = agency.getTemplate();
//                String hash = orderHash;
//                String errorMsg = "Ha ocurrido un problema al intentar acceder a la página de pago. Por favor, intente nuevamente más tarde.";
//                render("/templates/" + template + "/checkout/error.html", errorMsg, agency, template, order, hash);
//            }
//
//            renderText("Ha ocurrido un problema al intentar acceder a la página de pago. Por favor, intente nuevamente más tarde.");
//        }
//    }

}
