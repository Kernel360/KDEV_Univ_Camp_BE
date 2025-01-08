package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarGeoPointRequest {    // 지오펜싱 이벤트 정보 클래스
    private String mdn; // 차량 번호
    private String tid; // 터미널 아이디
    private String mid; // 제조사 아이디
    private String pv;  // 패킷 버전
    private String did; // 디바이스 아이디
    private String oTime;  // 발생일시
    private String geoGrpId;  // 지오펜스 그룹 아이디
    private String geoPId;  // 지오펜스 아이디
    private String evtVal;  // 이벤트 값
    private String gcd; // gps 상태
    private String lat; // gps 위도
    private String lon; // gps 경도
    private String ang; // 방향
    private String spd; // 속도
    private String sum; //누적 주행 거리
}
