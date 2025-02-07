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
        System.out.println("🚀 [로그인 요청] ID: " + loginDto.getId() + ", 비밀번호: " + loginDto.getPassword());

        // ✅ 사용자가 입력한 ID를 기반으로 user_id(UUID) 조회
        User user = userRepository.findById(loginDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        String userUUID = user.getUserId(); // ✅ UUID 조회
        System.out.println("✅ 조회된 user_id(UUID): " + userUUID);

        // ✅ user_authority 테이블에서 user_id(UUID)가 존재하는지 확인
        boolean hasAuthority = userAuthorityRepository.existsByUserUserId(userUUID);
        System.out.println("✅ user_authority 조회 결과 (UUID 존재 여부): " + hasAuthority);

        if (!hasAuthority) {
            throw new IllegalArgumentException("권한이 존재하지 않는 사용자입니다.");
        }

        // ✅ 비밀번호 검증 (암호화된 비밀번호와 비교)
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.out.println("❌ 비밀번호가 일치하지 않습니다.");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("✅ 비밀번호 검증 통과");

        // ✅ 사용자 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // ✅ 권한 변환 (getAuthorityName() → getAuthority())
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())) // 변경된 부분
                .collect(Collectors.toList());

        if (grantedAuthorities.isEmpty()) {
            System.out.println("⚠️ 사용자 권한이 없어서 기본 권한 추가 (ROLE_USER)");
            grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // ✅ UUID를 기반으로 인증 토큰 생성 (비밀번호 제거, Spring Security에서 재인증 수행)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userUUID, null, grantedAuthorities);

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

        // ✅ user_id(UUID)를 기반으로 JWT 생성
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
