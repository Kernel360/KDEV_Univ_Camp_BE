package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {

    private String type;
    private String date;

    @JsonProperty("time") // JSON 필드와 일치시키기
    private String time;

    @JsonProperty("vehicle_id") // JSON 필드와 일치시키기
    private String vehicleId;

    private Double latitude;
    private Double longitude;
}
