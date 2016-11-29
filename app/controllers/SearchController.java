package controllers;

import play.Play;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.mvc.Controller;

import com.google.gson.JsonElement;

import dto.ErrorDto;

public class SearchController extends Controller {

    public static void getAutocomplete(String q) {
       
    	
    	try {
	    	
	    	String url =  Play.configuration.getProperty("api.flight.url")+"/v1/getAutocomplete?q="+q;
	    	System.out.println("pegandole a: "+url);
	    	WSRequest request = WS.url(url);
	    	
	    	//request.authenticate(paymentApiUser, paymentApiPassword);
			
			JsonElement response = request.get().getJson();
			System.out.println("sending response: "+"{\"items\":"+response.toString()+"}");
			renderText("{\"items\":"+response.toString()+"}");
			
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString());
    		ErrorDto errorDto = new ErrorDto(e.getMessage());
        	renderJSON(errorDto);
		}
    	
    }

}