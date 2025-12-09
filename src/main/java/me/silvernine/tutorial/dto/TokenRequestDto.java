package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    private String mdn;
    private String tid;
    private String mid;
    private String pv;
    private String did;
    private String dfWer;
}
