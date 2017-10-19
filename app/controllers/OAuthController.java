package controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
    
    public static void renderBancoChileLogin(String transactionId, String selectedCurrency, String agencyId, String agencySlug, String step, String tknumber) {
        try {
        	System.out.println("agencyId: "+agencyId);
        	String token = CrossLoginUtils.getTransactionToken(agencyId,"test");
        	apiCrossToken = token;
            System.out.println("Token obtenido de API CROSSLOGIN: " + token);
            //valor1;valor2;valor3;valorN;tokenDeAplicación;agencia
            String state = "";
            if (Strings.isNullOrEmpty(tknumber)){
            	state = "notk"+";"+transactionId+";"+step+";"+token+";"+agencySlug;            	
            }
            else {
            	state = tknumber+";"+transactionId+";"+step+";"+token+";"+agencySlug;
            }
            System.out.println("state que se enviara a crossLogin: "+state);
            
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
    
    
    public static void renderBancoChileLoginToTravelpay(String transactionId, String selectedCurrency, String agencyId, String agencySlug) {
        try {
        	System.out.println("agencyId: "+agencyId);
        	String token = CrossLoginUtils.getTransactionToken(agencyId,"test");
        	apiCrossToken = token;
            System.out.println("Token obtenido de API CROSSLOGIN: " + token);
            //valor1;valor2;valor3;valorN;tokenDeAplicación;agencia
            String step = "checkout";
            String state = "notk;"+transactionId+";"+step+";"+token+";"+agencySlug;
            System.out.println("state que se enviara a crossLogin: "+state);
            
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
      String tknumber = stateDecrypted.split(";")[0];
      String transactionId = stateDecrypted.split(";")[1];
      String step = stateDecrypted.split(";")[2];
      String token = stateDecrypted.split(";")[3];
      System.out.println("se guarda para transactionId: "+transactionId+", token: "+token);
      Cache.set(transactionId, token, "1d");
      
      if (Strings.isNullOrEmpty(tknumber) || tknumber.equals("notk")){
	      if (step.equals("index"))
	    	  FlightsController.reloadWithTransaction(transactionId,null);
	      else
	    	  PaymentFlowController.reloadWithTransaction();
      }
      else {
    	  if (step.equals("index")){
    		  FlightsController.reloadWithTransaction(transactionId,tknumber);
    	  }
    	  else {
    		  FlightsController.reloadWithTransaction(transactionId,null);
    	  }
      }
      
    }
    
    public static void logout(String transactionId) {
    	System.out.println("transactionId logout: "+transactionId);
    	Cache.delete(transactionId);
    	session.clear();
    	renderText("ok");
    	
    }
}
