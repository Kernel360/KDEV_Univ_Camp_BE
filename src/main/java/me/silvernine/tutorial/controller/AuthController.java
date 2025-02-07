package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.silvernine.tutorial.dto.LoginDto;
import me.silvernine.tutorial.dto.TokenDto;
import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserService userService, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * ✅ 회원가입 API
     */
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    /**
     * ✅ 로그인 API (JWT 발급)
     */
    @Operation(summary = "로그인", description = "로그인을 한 후 JWT 토큰을 반환합니다.")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        System.out.println("🚀 [로그인 요청] ID: " + loginDto.getId() + ", 비밀번호: " + loginDto.getPassword());

        // ✅ 사용자가 입력한 id로 user_id(UUID) 조회
        User user = userRepository.findById(loginDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        String userUUID = user.getUserId(); // ✅ UUID 기반으로 로그인
        System.out.println("✅ 조회된 user_id(UUID): " + userUUID);

        // ✅ 비밀번호 검증
        if (!userService.validatePassword(loginDto.getId(), loginDto.getPassword())) {
            System.out.println("❌ 비밀번호가 일치하지 않습니다.");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("✅ 비밀번호 검증 통과");

        // ✅ 인증 토큰 생성 (UUID 사용)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userUUID, loginDto.getPassword());

        System.out.println("✅ 인증 토큰 생성 완료");

        // ✅ Spring Security에서 인증 수행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("✅ Spring Security 인증 성공: " + authentication.getName());
        } catch (Exception e) {
            System.out.println("❌ Spring Security 인증 실패: " + e.getMessage());
            throw new IllegalArgumentException("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }

        // ✅ JWT 생성 (UUID 사용)
        String nickname = user.getNickname();
        String jwt = tokenProvider.createToken(authentication, nickname);
        System.out.println("✅ JWT 생성 결과: " + jwt);

        if (jwt == null) {
            throw new RuntimeException("❌ JWT 생성 실패!");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.ok().headers(httpHeaders).body(new TokenDto(jwt));
    }
}
