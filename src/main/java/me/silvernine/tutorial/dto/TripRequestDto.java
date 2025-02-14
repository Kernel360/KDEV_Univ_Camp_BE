package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {

    @JsonProperty("type")  // ✅ JSON의 "type" 필드와 매핑
    private String type;

    @JsonProperty("vehicle_id")  // ✅ JSON의 "vehicle_id" 필드와 매핑
    private String vehicleId;

    @JsonProperty("time")  // ✅ JSON의 "time" 필드를 Java의 timestamp 필드로 변환
    private String timestamp;  // JSON에서는 "time"이지만, 실제로는 timestamp 필드로 처리

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("battery_level")
    private Integer batteryLevel;
}
