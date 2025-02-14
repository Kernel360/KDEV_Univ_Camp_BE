package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    private String type;
    private String date;

    @JsonProperty("time")
    private String time;  // 🔥 'time' 값을 변환해야 함

    @JsonProperty("vehicle_id")
    private String vehicleId;

    private Double latitude;
    private Double longitude;
}
