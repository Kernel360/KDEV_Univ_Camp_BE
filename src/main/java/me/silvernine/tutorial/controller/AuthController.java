package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.silvernine.tutorial.dto.LoginDto;
import me.silvernine.tutorial.dto.TokenDto;
import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.repository.UserAuthorityRepository;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserService userService, UserRepository userRepository,
                          UserAuthorityRepository userAuthorityRepository, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.passwordEncoder = passwordEncoder;
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
        System.out.println("🚀 [로그인 요청] ID: " + loginDto.getId());

        // ✅ 사용자가 입력한 ID(username)를 기반으로 user_id(UUID) 조회
        User user = userRepository.findById(loginDto.getId())
                .or(() -> userRepository.findByUserId(loginDto.getId())) // ✅ UUID 조회 추가
                .orElseThrow(() -> {
                    System.out.println("❌ [ERROR] 사용자가 존재하지 않음: " + loginDto.getId());
                    return new IllegalArgumentException("사용자가 존재하지 않습니다.");
                });

        System.out.println("✅ 조회된 user_id(UUID): " + user.getUserId());

        // ✅ 비밀번호 검증 (암호화된 비밀번호와 비교)
        System.out.println("🔍 [DEBUG] 입력된 비밀번호: " + loginDto.getPassword());
        System.out.println("🔍 [DEBUG] 저장된 암호화된 비밀번호: " + user.getPassword());

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.out.println("❌ 비밀번호가 일치하지 않습니다.");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("✅ 비밀번호 검증 통과");

        // ✅ 사용자 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        System.out.println("🔍 [DEBUG] 사용자 권한 리스트: " + authorities);

        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        if (grantedAuthorities.isEmpty()) {
            System.out.println("⚠️ 사용자 권한이 없어서 기본 권한 추가 (ROLE_USER)");
            grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // ✅ UUID를 기반으로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserId(), null, grantedAuthorities);

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("✅ Spring Security 인증 성공");

            // ✅ JWT 생성 및 반환
            String jwt = tokenProvider.createToken(authentication, user.getNickname());
            System.out.println("✅ JWT 생성 결과: " + jwt);

            if (jwt == null || jwt.isEmpty()) {
                System.out.println("❌ JWT 생성 실패!");
                throw new RuntimeException("JWT 생성 실패!");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return ResponseEntity.ok().headers(httpHeaders).body(new TokenDto(jwt));
        } catch (Exception e) {
            System.out.println("❌ 인증 과정 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("인증 실패: " + e.getMessage(), e);
        }
    }
}
