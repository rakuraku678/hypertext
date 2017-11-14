package controllers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
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
//	    String st = "['ff_ff_e', 'ff_ff', 'ee_ee', 'ee_ee', 'ee___', 'ee_ee', 'ee_ee', 'ee_ee', 'eeeee']";
//	    jsonSeats.getAsJsonObject().addProperty("stringloco", st);
	    JsonElement parsedSeats = parseResults(jsonSeats);
        renderJSON(parsedSeats);
    }
    
    @Util
    public static JsonElement parseResults(JsonElement jsonSeats){
    	JsonElement results = jsonSeats;
    	
    	int fila = 1;
    	int columna = 1;
    	StringBuilder seatsRow = new StringBuilder("['");
		StringBuilder unavailableSeats = new StringBuilder("['");
		String[] seatsLabels= {"A","B","C","D","E","F","G","H","I","J","K"};
		
		
		JsonArray cabinsArray = jsonSeats.getAsJsonObject().get("cabins").getAsJsonArray();
//		int columnNumber = 0;
//		for (JsonElement cabinElement : cabinsArray) {
//			
//			
//			JsonArray columnsDataArray = cabinElement.getAsJsonObject().get("columns").getAsJsonArray();
//			columnsDataArray.get(columnNumber).getAsJsonObject().get("type").getAsString().equals("Aisle");
//		}
		
		for (JsonElement cabinElement : cabinsArray) {
			JsonArray economyRows = cabinElement.getAsJsonObject().get("rows").getAsJsonArray();
			JsonArray columnsDataArray = cabinElement.getAsJsonObject().get("columns").getAsJsonArray();
	    	for (JsonElement row : economyRows) {
	    		columna=0;
	    		int seatNumber = 0;
	    		if (row.getAsJsonObject().has("seats")){
		    		JsonArray seats = row.getAsJsonObject().get("seats").getAsJsonArray();
		    		for (JsonElement seat : seats) {
		    			int colChecker=columna;
		    			while (columnsDataArray.size()>colChecker && columnsDataArray.get(colChecker).getAsJsonObject().get("type").getAsString().equals("Aisle")) {
		    				seatsRow.append("_");
		    				colChecker++;
		    			}
		    			seatsRow.append("e");
		    			columna++;
		    			seatNumber++;
//		    			if (columna==8){//
//		        			seatsRow.append("_");
//		        			columna++;
//		        		}

		    			if (seat.getAsJsonObject().get("occupiedInd").getAsBoolean() || seat.getAsJsonObject().get("inoperativeInd").getAsBoolean()) {
		    				unavailableSeats.append(fila+"_"+seatsLabels[seatNumber]+"','");
		    			}
			    		
					}
	    		}
	    		else {
	    			seatsRow.append("_");
	    		}
	    		seatsRow.append("','");
	    		fila++;
			}
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