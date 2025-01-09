package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingRequestDto {
    private String mdn;    //차량 번호
    private String oTime;  //발생 일시
    private String ctrCnt; //제어명령 개수
    private String geoCnt; //지오펜싱 설정 개수
}
