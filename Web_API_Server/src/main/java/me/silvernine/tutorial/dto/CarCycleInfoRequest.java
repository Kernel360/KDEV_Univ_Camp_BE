package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CarCycleInfoRequest {

    private String mdn; // 차량 번호
    private String tid; // 터미널 아이디
    private String mid; // 제조사 아이디
    private String pv;  // 패킷 버전
    private String did; // 디바이스 아이디
    private String oTime;  // 발생 시간
    private String cCnt;  // 주기 정보 갯수
    private List<CycleList> cList; // 패킷 버전

    @Getter
    @Setter
    @Builder
    public static class CycleList {
        private String sec; // 발생 시간 "초"
        private String gcd; // gps 상태
        private String lat; // gps 위도
        private String lon; // gps 경도
        private String ang; // 방향
        private String spd; // 속도
        private String sum; //누적 주행 거리
        private String bat; // 배터리 전압

    }

}
