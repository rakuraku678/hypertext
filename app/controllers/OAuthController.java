package controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;

import play.Logger;
import play.cache.Cache;
import play.mvc.Controller;
import utils.AESEncryptorUtil;
import utils.CacheUtils;
import utils.CrossLoginUtils;

public class OAuthController extends Controller {

	
	private static final String OAUTH_SERVER_URL = "http://190.98.204.155:7001/AuthServer/oauth/authorize";//";//https://200.14.140.84:4443/AuthServer/oauth/authorize"; //http://190.98.204.155:7001/AuthServer/oauth/authorize";//
	private static final String OAUTH_SERVER_URL_PROD = "https://servicios.bancochile.cl/AuthServer/authorization";
    private static final String AES_KEY = "Bar12345Bar12345";
    public static String apiCrossToken = ""; 
    
    public static void renderBancoChileLogin(String transactionId, String promoSlug, String selectedCurrency, String agencyId,String agencySlug) {
        try {
        	
        	String token = CrossLoginUtils.getTransactionToken(agencyId,"test");
        	apiCrossToken = token;
            Logger.info("Token obtenido de API CROSSLOGIN: " + token);
            //valor1;valor2;valor3;valorN;tokenDeAplicación;agencia
            
            String state = transactionId+";"+token+";"+agencySlug;
            System.out.println("state que se enviara: "+state);
            
            state = AESEncryptorUtil.encrypt(state, AES_KEY);
            
            String clientId = "travel_club";

            String callbackUrl = "";
            String travelClubLoginUrl = OAUTH_SERVER_URL + "?response_type=code&state=" + state + "&client_id=" + clientId + "&scope=read+write+delete";

            Logger.info("Url de login a banco: " + travelClubLoginUrl);
            render("/PaymentFlowController/redirectToLogin.html", travelClubLoginUrl, callbackUrl);

        } catch (Exception ex){
        	ex.printStackTrace();
        }
    }
    
    public static void bancoChileEndPoint(String state) throws GeneralSecurityException, IOException {
      Logger.info("Datos recibidos de bch luego del login:");
      Logger.info("state: " + state);
      String stateDecrypted = AESEncryptorUtil.decrypt(state, AES_KEY);
      System.out.println("State decrypted: "+stateDecrypted);
      String transactionId = stateDecrypted.split(";")[0];
      String token = stateDecrypted.split(";")[1];
      Cache.set(transactionId, token, "1d");
      PaymentFlowController.reloadWithTransaction();
      
    }

}
