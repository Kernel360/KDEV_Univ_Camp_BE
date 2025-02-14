package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("vehicle_id")  // JSON의 "vehicle_id" 필드와 매핑
    private String vehicleId;

    @JsonProperty("date")  // JSON의 "date" 필드 추가
    private String date;

    @JsonProperty("time")  // JSON의 "time" 필드와 매핑
    private String timestamp;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("battery_level")
    private Integer batteryLevel;
}
