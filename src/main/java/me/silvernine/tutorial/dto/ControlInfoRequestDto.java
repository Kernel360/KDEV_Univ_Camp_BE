package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private String oTime;     // 발생 시간
    private String cCnt;      // 주기 데이터 개수
    private List<CycleData> cList; // 주기 데이터 리스트

    @Getter
    @Setter
    public static class CycleData {
        private String sec; // 초 단위
        private String gcd; // GPS 상태
        private String lat; // 위도
        private String lon; // 경도
        private String ang; // 방향
        private String spd; // 속도
        private String sum; // 누적 주행 거리
        private String bat; // 배터리 전압
    }
}
