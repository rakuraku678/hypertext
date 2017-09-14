package utils.dtos;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class CabinConfigurationDto implements Dto {

    Map<String,String> cabins;
    private static final Map<String, String> cabinCodesMap;
    static
    {
        cabinCodesMap = new HashMap<String, String>();
        cabinCodesMap.put("Y", "Económica");
        cabinCodesMap.put("S", "Económica Premium");
        cabinCodesMap.put("C", "Ejecutiva");
        cabinCodesMap.put("J", "Ejecutiva Premium");
        cabinCodesMap.put("F", "Primera");
        cabinCodesMap.put("P", "Primera Premium");
    }

    public static CabinConfigurationDto parseCabinConfigurationDto(JsonElement responseJson){

        CabinConfigurationDto cabinConfigurationDto = new CabinConfigurationDto();
        cabinConfigurationDto.cabins = Maps.newLinkedHashMap();
        JsonArray jsonOArray = responseJson.getAsJsonArray();
        for (int i = 0; i < jsonOArray.size(); i++) {
            String key = jsonOArray.get(i).getAsString();
            cabinConfigurationDto.cabins.put(key, cabinCodesMap.get(key));
        }
        return cabinConfigurationDto;
    }
}