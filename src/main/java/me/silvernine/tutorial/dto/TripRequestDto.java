package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    private String type;
    private String date;
    private String time;  // ✅ 'time' 값 유지 (getter가 정상 동작하도록 확인)
    private String vehicleId;  // ✅ 'vehicle_id' → 'vehicleId'로 변경
    private Double latitude;
    private Double longitude;
}
