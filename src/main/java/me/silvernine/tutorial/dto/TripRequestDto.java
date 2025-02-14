package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {

    @Schema(description = "데이터 유형 (예: RM)", example = "RM")
    @JsonProperty("type")
    private String type;

    @Schema(description = "차량 ID", example = "12가 1234")
    @JsonProperty("vehicle_id")
    private String vehicleId;

    @Schema(description = "GPS 기록 시간", example = "2024-11-30 00:01:20.00")
    @JsonProperty("time")
    private String timestamp;  // JSON의 "time" 필드를 timestamp로 매핑

    @Schema(description = "위도 값", example = "35.624403")
    @JsonProperty("latitude")
    private Double latitude;

    @Schema(description = "경도 값", example = "129.335968")
    @JsonProperty("longitude")
    private Double longitude;

    @Schema(description = "배터리 레벨", example = "100")
    @JsonProperty("battery_level")
    private Integer batteryLevel;
}
