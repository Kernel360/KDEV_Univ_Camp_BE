package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.service.TokenService;
import me.silvernine.tutorial.util.TokenValidator;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Token Management", description = "APIs for managing and validating tokens")  // ✅ Swagger 태그 추가
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService; // ✅ 토큰 생성 서비스
    private final TokenValidator tokenValidator; // ✅ 토큰 검증 유틸

    /**
     * JWT 토큰 생성 API
     * @param request 클라이언트 요청 정보 (MDN, TID 등)
     * @return 생성된 JWT 토큰
     */
    @Operation(summary = "Generate a new token", description = "Receives client credentials and returns a generated token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/getToken")
    public ResponseEntity<TokenResponseDto> getToken(@RequestBody TokenRequestDto request) {
        TokenResponseDto response = tokenService.generateToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * JWT 토큰 유효성 검사 API
     * @param token 클라이언트가 보낸 JWT 토큰
     * @return 유효하면 "000" (성공), 유효하지 않으면 "100" (토큰 오류)
     */
    @Operation(summary = "Validate token", description = "Checks if the provided token is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenResponseDto> validateToken(@RequestParam String token) {
        // ✅ "Bearer "가 포함되어 있으면 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 이후의 순수한 토큰 값만 추출
        }

        boolean isValid = tokenValidator.validate(token); // ✅ 인스턴스 메서드로 호출

        if (isValid) {
            return ResponseEntity.ok().body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.SUCCESS)  // ✅ "000" 반환
                            .rstMsg("Token is valid")
                            .token(token)
                            .build()
            );
        } else {
            return ResponseEntity.ok().body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.INVALID_TOKEN)  // ✅ "100" 반환
                            .rstMsg("Invalid or expired token")
                            .build()
            );
        }
    }
}
