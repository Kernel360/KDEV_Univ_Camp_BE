package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlInfoRequestDto {
    private String mdn;       // 차량 번호
    private String tid;       // 터미널 아이디
    private String mid;       // 제조사 아이디
    private String pv;        // 패킷 버전
    private String did;       // 디바이스 아이디
    private String onTime;    // 차량 시동 On 시간
    private String dFWVer;    // 디바이스 펌웨어 버전
}
