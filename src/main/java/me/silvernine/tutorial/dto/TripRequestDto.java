package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    private String type;
    private String date;
    private String time;  // 🔥 'time' 값을 변환해야 함
    private String vehicle_id;
    private Double latitude;
    private Double longitude;
}
