package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String rstCd;
    private String rstMsg;
    private String mdn;
    private String token;
    private String exPeriod;
}
