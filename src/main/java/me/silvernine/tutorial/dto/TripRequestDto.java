package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TripRequestDto {
    private String type;
    private String date;

    @JsonProperty("vehicle_id") // ✅ JSON 필드명 매핑
    private String vehicleId;

    @JsonProperty("time") // ✅ JSON 필드명 매핑
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SS") // ✅ JSON → LocalDateTime 변환
    private LocalDateTime timestamp;

    private double latitude;
    private double longitude;
}
