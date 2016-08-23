package utils.ApiFlightsSdk.v1;

import com.google.gson.JsonElement;
import play.libs.WS;

public class FlightsSearch extends ApiFlightsSDKBase {

    private static final String ENDPOINT = "/v1/flights/search";

    public String origin;
    public String destination;
    public String departuredate;
    public String returndate;
    public String passengercount;

    public JsonElement process(){

        WS.WSRequest request = prepareRequest(ENDPOINT);

        request.setParameter("origin", origin);
        request.setParameter("destination", destination);
        request.setParameter("departuredate", departuredate);
        request.setParameter("returndate", returndate);
        request.setParameter("passengercount", passengercount);

        JsonElement responseJsonObject = processResponse(request);

        return responseJsonObject;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDeparturedate(String departuredate) {
        this.departuredate = departuredate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public void setPassengercount(String passengercount) {
        this.passengercount = passengercount;
    }

}
