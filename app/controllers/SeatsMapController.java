package controllers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import play.mvc.Controller;
import play.mvc.Util;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.ApiFlightsSdk.v1.SeatsMap;

public class SeatsMapController extends Controller {

    public static void getSeatsBACK(String body) {
	    JsonElement bodyJsonElement = new JsonParser().parse(body);
	    SeatsMap seatsMap = new SeatsMap();
	    JsonElement jsonSeats = seatsMap.process(bodyJsonElement);

	    Template template = TemplateLoader.load(template("tags/checkout/seatsMapChart.html"));
        Map m = Maps.newHashMap();
        m.put("seatsMap", jsonSeats);
        renderHtml(template.render(m).replaceAll("\\s{2,}"," "));
    }
    

    public static void getSeats(String body) {
	    JsonElement bodyJsonElement = new JsonParser().parse(body);
	    SeatsMap seatsMap = new SeatsMap();
	    JsonElement jsonSeats = seatsMap.process(bodyJsonElement);
	    String st = "['ff_ff_e', 'ff_ff', 'ee_ee', 'ee_ee', 'ee___', 'ee_ee', 'ee_ee', 'ee_ee', 'eeeee']";
	    jsonSeats.getAsJsonObject().addProperty("stringloco", st);
	    JsonElement parsedSeats = parseResults(jsonSeats);
        renderJSON(parsedSeats);
    }
    
    @Util
    public static JsonElement parseResults(JsonElement jsonSeats){
    	JsonElement results = jsonSeats;
    	JsonArray economyRows = jsonSeats.getAsJsonObject().get("cabins").getAsJsonArray().get(0).getAsJsonObject().get("rows").getAsJsonArray();
    	int fila = 1;
    	int columna = 1;
    	StringBuilder seatsRow = new StringBuilder("['");
		StringBuilder unavailableSeats = new StringBuilder("['");
    	for (JsonElement row : economyRows) {
    		columna=1;
    		
    		if (row.getAsJsonObject().has("seats")){
	    		JsonArray seats = row.getAsJsonObject().get("seats").getAsJsonArray();
	    		for (JsonElement seat : seats) {
	    			if (columna==9){//sacar esto para no usar un asiento posta, se le esta poniendo _ a un asiento de verdad
	        			seatsRow.append("_");
	        		}
	    			else {
	    				seatsRow.append("e");
	    			
		    			if (seat.getAsJsonObject().get("occupiedInd").getAsBoolean() || seat.getAsJsonObject().get("inoperativeInd").getAsBoolean()) {
		    				unavailableSeats.append(fila+"_"+columna+"','");
		    			}
	    			}
		    		columna++;
				}
    		}
    		else {
    			seatsRow.append("_");
    		}
    		seatsRow.append("','");
    		fila++;
		}
    	unavailableSeats.setLength(unavailableSeats.length() - 2);
    	unavailableSeats.append("]");
    	
    	seatsRow.setLength(seatsRow.length() - 2);
    	seatsRow.append("]");
    	
    	String unavailable = unavailableSeats.toString();
    	String totalSeats = seatsRow.toString();
    	System.out.println("unavailable: "+unavailable);
    	System.out.println("totalSeats: "+totalSeats);
    	results.getAsJsonObject().addProperty("totalSeats", totalSeats);
    	results.getAsJsonObject().addProperty("unavailableSeats", unavailable);
    	return results;
    }
    
}