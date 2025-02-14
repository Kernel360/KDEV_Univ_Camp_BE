package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    @JsonProperty("vehicleId")
    private String vehicleId;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("battery_level")  // ✅ 배터리 수치 추가
    private Integer batteryLevel;
}
