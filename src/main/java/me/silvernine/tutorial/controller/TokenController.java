package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.service.TokenService;
import me.silvernine.tutorial.util.TokenValidator;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import java.util.Date;

@Tag(name = "Token Management", description = "APIs for managing and validating tokens")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService; // ✅ 토큰 생성 서비스 (필요 없으면 삭제 가능)
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
     * @param request HTTP 요청 (Authorization 헤더에서 JWT 토큰 추출)
     * @return 유효하면 "000" (성공), 유효하지 않으면 "100" (토큰 오류)
     */
    @Operation(summary = "Validate token", description = "Checks if the provided token is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenResponseDto> validateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        // ✅ Authorization 헤더가 없거나 "Bearer " 접두사가 없으면 401 반환
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.INVALID_TOKEN)
                            .rstMsg("Unauthorized: No valid token provided")
                            .build()
            );
        }

        // ✅ "Bearer " 접두어 제거 후 실제 토큰 값 추출
        String token = authorizationHeader.substring(7);

        boolean isValid = tokenValidator.validate(token);

        if (!isValid) {
            return ResponseEntity.ok().body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.INVALID_TOKEN)
                            .rstMsg("Invalid or expired token")
                            .build()
            );
        }

        // ✅ SecurityContext에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 사용자 ID

        // ✅ JWT 만료 시간 가져오기
        Claims claims = tokenValidator.getClaims(token);
        Date expiration = claims.getExpiration();
        long expPeriod = expiration.getTime(); // 만료 시간 (Unix Timestamp)

        return ResponseEntity.ok().body(
                TokenResponseDto.builder()
                        .rstCd(ResponseCode.SUCCESS)
                        .rstMsg("Token is valid")
                        .token(token)
                        .username(username)  // 사용자 ID 추가
                        .exPeriod(expPeriod) // 만료 시간 추가
                        .build()
        );
    }
}
