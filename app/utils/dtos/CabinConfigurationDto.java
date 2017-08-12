package utils.dtos;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.*;

public class CabinConfigurationDto implements Dto{

    Map<String,String> cabins;

    public static CabinConfigurationDto parseCabinConfigurationDto(JsonElement responseJson){
        CabinConfigurationDto cabinConfigurationDto = new CabinConfigurationDto();
        cabinConfigurationDto.cabins = new HashMap<String, String>();
        JsonArray jsonOArray = responseJson.getAsJsonArray();
        for (int i = 0; i < jsonOArray.size(); i++) {
            String key = jsonOArray.get(i).getAsString();
            cabinConfigurationDto.cabins.put(key,CabinCodes.valueOf(key).description);
        }

        return cabinConfigurationDto;
    }

    private enum CabinCodes {

        P("Primera Clase Premium"),
        F("Primera Clase"),
        J("Clase Ejecutiva Premium"),
        C("Clase Ejecutiva"),
        S("Economica Premium"),
        Y("Turista");

        private final String description;

        CabinCodes(String description) {
            this.description = description;
        }
    }
}
