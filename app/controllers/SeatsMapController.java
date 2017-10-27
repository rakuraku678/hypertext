package controllers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import play.mvc.Controller;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.ApiFlightsSdk.v1.SeatsMap;

public class SeatsMapController extends Controller {

    public static void getSeats(String body) {
	    JsonElement bodyJsonElement = new JsonParser().parse(body);
	    SeatsMap seatsMap = new SeatsMap();
	    JsonElement jsonSeats = seatsMap.process(bodyJsonElement);

	    Template template = TemplateLoader.load(template("tags/checkout/seatsMapChart.html"));
        Map m = Maps.newHashMap();
        m.put("seatsMap", jsonSeats);
        renderHtml(template.render(m).replaceAll("\\s{2,}"," "));
    }
}