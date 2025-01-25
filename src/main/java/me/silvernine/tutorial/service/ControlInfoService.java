package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.dto.ControlInfoResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlInfoService {

    // 기존 getControlInfo 메서드 유지
    public ControlInfoResponseDto getControlInfo(ControlInfoRequestDto request) {
        // 입력값 검증
        if (request.getMdn() == null || request.getTid() == null) {
            ControlInfoResponseDto response = new ControlInfoResponseDto();
            response.setRstCd("301"); // Required parameter error
            response.setRstMsg("Required parameter missing");
            return response;
        }

        // Mock 데이터 생성
        ControlInfoResponseDto response = new ControlInfoResponseDto();
        response.setRstCd("000");
        response.setRstMsg("Success");
        response.setMdn(request.getMdn());
        response.setOTime("20210901174045");
        response.setCtrCnt("2");
        response.setGeoCnt("2");

        // 제어 리스트 예시
        ControlInfoResponseDto.ControlList control1 = new ControlInfoResponseDto.ControlList();
        control1.setCtrId("1");
        control1.setCtrCd("05");
        control1.setCtrVal("120");

        ControlInfoResponseDto.ControlList control2 = new ControlInfoResponseDto.ControlList();
        control2.setCtrId("2");
        control2.setCtrCd("05");
        control2.setCtrVal("120");

        response.setCtrList(List.of(control1, control2));

        // 지오펜싱 리스트 예시
        ControlInfoResponseDto.GeoList geo1 = new ControlInfoResponseDto.GeoList();
        geo1.setGeoCtrId("267");
        geo1.setUpVal("0");
        geo1.setGeoGrpId("1");
        geo1.setGeoEvtTp("3");
        geo1.setGeoRange("50");
        geo1.setLat("4140338");
        geo1.setLon("217403");
        geo1.setOnTime("20210901090000");
        geo1.setOffTime("20210901235959");
        geo1.setStoreTp("1");

        ControlInfoResponseDto.GeoList geo2 = new ControlInfoResponseDto.GeoList();
        geo2.setGeoCtrId("268");
        geo2.setUpVal("0");
        geo2.setGeoGrpId("1");
        geo2.setGeoEvtTp("3");
        geo2.setGeoRange("50");
        geo2.setLat("4140338");
        geo2.setLon("217403");
        geo2.setOnTime("20210901090000");
        geo2.setOffTime("20210901235959");
        geo2.setStoreTp("1");

        response.setGeoList(List.of(geo1, geo2));

        return response;
    }

    // 새로운 processCycleInfo 메서드 추가
    public boolean processCycleInfo(ControlInfoRequestDto request) {
        if (request.getCList() == null || request.getCList().isEmpty()) {
            return false; // 데이터가 없을 경우 실패 처리
        }

        // 요청 데이터를 처리
        request.getCList().forEach(data -> {
            System.out.println("Processing cycle data: " + data.toString());
        });

        return true; // 성공적으로 처리한 경우
    }
}
