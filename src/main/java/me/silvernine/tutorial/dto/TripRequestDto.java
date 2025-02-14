package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripRequestDto {
    private String vehicleId;
    private double latitude;
    private double longitude;
    private int batteryLevel;

    // ✅ timestamp 자동 변환 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime timestamp;
}
