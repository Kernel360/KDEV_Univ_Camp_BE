package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    private String mdn;  // 차량 번호
    private String tid;  // 단말 ID
    private String mid;  // 제조사 아이디
    private String pv;   // 패킷 버전
    private String did;  // 디바이스 ID
    private String dfWer; // 디바이스 펌웨어 버전
}