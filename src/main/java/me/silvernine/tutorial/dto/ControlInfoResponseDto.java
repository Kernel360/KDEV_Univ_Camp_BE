package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ControlInfoResponseDto {
    private String rstCd;
    private String rstMsg;
    private String mdn;
    private String oTime;
    private String ctrCnt;
    private String geoCnt;
    private List<ControlList> ctrList;
    private List<GeoList> geoList;

    @Getter
    @Setter
    public static class ControlList {
        private String ctrId;
        private String ctrCd;
        private String ctrVal;
    }

    @Getter
    @Setter
    public static class GeoList {
        private String geoCtrId;
        private String upVal;
        private String geoGrpId;
        private String geoEvtTp;
        private String geoRange;
        private String lat;
        private String lon;
        private String onTime;
        private String offTime;
        private String storeTp;
    }
}
