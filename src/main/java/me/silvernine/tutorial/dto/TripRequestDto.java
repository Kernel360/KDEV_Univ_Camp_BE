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

    @Schema(description = "날짜 (현재 빈 값이 들어오므로 무시)", example = "")
    @JsonProperty("date")
    private String date;  // 현재 빈 값이지만 요청 JSON에 포함되므로 추가

    @Schema(description = "GPS 기록 시간 (날짜+시간)", example = "2024-11-30 00:01:20.00")
    @JsonProperty("time")
    private String time;  // **기존 timestamp 대신 time 사용**

    @Schema(description = "차량 ID", example = "12가 1234")
    @JsonProperty("vehicle_id")
    private String vehicleId;

    @Schema(description = "위도 값", example = "35.624403")
    @JsonProperty("latitude")
    private Double latitude;

    @Schema(description = "경도 값", example = "129.335968")
    @JsonProperty("longitude")
    private Double longitude;

    @Schema(description = "배터리 레벨 (옵션)", example = "100")
    @JsonProperty("battery_level")
    private Integer batteryLevel;
}
