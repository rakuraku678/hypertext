package utils;

import utils.dtos.Dto;

public class FlightsDataDto implements Dto {

    public String data;
    public String html;
    public String status;

    public FlightsDataDto(String data, String html, String status) {
        this.data = status;
        this.html = html;
        this.status = status;
    }

}