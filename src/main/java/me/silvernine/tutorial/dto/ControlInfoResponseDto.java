package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ControlInfoResponseDto {
    private String rstCd;            // 결과 코드
    private String rstMsg;           // 결과 메시지
    private String mdn;              // 차량 번호
    private String oTime;            // 발생 일시
    private String ctrCnt;           // 제어명령 개수
    private String geoCnt;           // 지오펜싱 설정 개수
    private List<ControlList> ctrList; // 제어 리스트
    private List<GeoList> geoList;     // 지오펜싱 리스트

    @Getter
    @Setter
    public static class ControlList {
        private String ctrId;    // 제어 아이디
        private String ctrCd;    // 제어 코드
        private String ctrVal;   // 제어 값
    }

    @Getter
    @Setter
    public static class GeoList {
        private String geoCtrId; // 지오펜스 아이디
        private String upVal;    // 업데이트 값
        private String geoGrpId; // 그룹 아이디
        private String geoEvtTp; // 이벤트 타입
        private String geoRange; // 지오펜스 반경
        private String lat;      // 위도
        private String lon;      // 경도
        private String onTime;   // 시작 시간
        private String offTime;  // 종료 시간
        private String storeTp;  // 저장 타입
    }
}

