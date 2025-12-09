package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.silvernine.tutorial.dto.AuthResponseDto;
import me.silvernine.tutorial.dto.LoginDto;
import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.repository.UserAuthorityRepository;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TokenProvider tokenProvider,
                          AuthenticationManager authenticationManager,
                          UserService userService,
                          UserRepository userRepository,
                          UserAuthorityRepository userAuthorityRepository,
                          PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @Operation(summary = "로그인", description = "로그인을 한 후 JWT 토큰과 닉네임을 반환합니다.")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        System.out.println("🚀 [로그인 요청] ID: " + loginDto.getId());

        User user = userRepository.findByIdEquals(loginDto.getId())
                .orElseThrow(() -> {
                    System.out.println("❌ [ERROR] 사용자가 존재하지 않음: " + loginDto.getId());
                    return new IllegalArgumentException("사용자가 존재하지 않습니다.");
                });

        System.out.println("조회된 user_id(UUID): " + user.getUserId());
        System.out.println("조회된 사용자 닉네임: " + user.getNickname());

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.err.println("비밀번호가 일치하지 않습니다.");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("비밀번호 검증 통과");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        if (grantedAuthorities.isEmpty()) {
            grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getId(), loginDto.getPassword(), grantedAuthorities);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Spring Security 인증 성공");

        String jwt = tokenProvider.createToken(authentication);

        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT 생성 실패");
        }
        System.out.println("[JWT 발급 성공] " + jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        AuthResponseDto responseDto = AuthResponseDto.builder()
                .token(jwt)
                .nickname(user.getNickname())
                .build();

        return ResponseEntity.ok().headers(httpHeaders).body(responseDto);
    }
}
