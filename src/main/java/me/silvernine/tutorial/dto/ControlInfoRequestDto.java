package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ControlInfoRequestDto {
    private String mdn;
    private String tid;
    private String mid;
    private String pv;
    private String did;
    private String onTime;
    private String dFWVer;
    private String oTime;
    private String cCnt;
    private List<CycleData> cList;

    @Getter
    @Setter
    public static class CycleData {
        private String sec;
        private String gcd;
        private String lat;
        private String lon;
        private String ang;
        private String spd;
        private String sum;
        private String bat;
    }
}
