package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {
    private String carName;   // 차량 이름
    private String carNumber; // 차량 번호
}