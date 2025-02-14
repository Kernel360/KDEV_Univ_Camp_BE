package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    @JsonProperty("type")  // ✅ JSON 필드명과 매핑
    private String type;

    @JsonProperty("vehicle_id")  // ✅ JSON 필드명과 매핑
    private String vehicleId;

    @JsonProperty("time")  // ✅ JSON 필드명과 매핑 (timestamp → time)
    private String time;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("battery_level")
    private Integer batteryLevel;
}
