package me.silvernine.tutorial.util;

/**
 * ResponseCode 클래스는 API 응답 코드(상태 코드)를 정의하는 상수 클래스입니다.
 * API 응답 시 성공 또는 오류 상태를 코드로 반환하기 위해 사용됩니다.
 */
public class ResponseCode {

    /**
     * 요청이 성공적으로 처리되었음을 나타내는 코드.
     */
    public static final String SUCCESS = "000";

    /**
     * 필수 요청 파라미터가 누락되었을 때 반환되는 코드.
     */
    public static final String MISSING_PARAMETER = "301";

    /**
     * 유효하지 않거나 만료된 토큰일 때 반환되는 코드.
     */
    public static final String INVALID_TOKEN = "100";
}
