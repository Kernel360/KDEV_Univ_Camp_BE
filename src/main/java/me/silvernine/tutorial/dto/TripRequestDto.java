package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {

    private String type;
    private String date;

    @JsonProperty("time") // ✅ JSON 필드와 매칭
    private String time;

    @JsonProperty("car_number") // ✅ 소문자로 변경 (일반적인 JSON 네이밍 컨벤션)
    private String carNumber;

    private Double latitude;
    private Double longitude;

    @JsonProperty("battery_level") // ✅ JSON 필드와 매칭
    private Integer batteryLevel;
}
