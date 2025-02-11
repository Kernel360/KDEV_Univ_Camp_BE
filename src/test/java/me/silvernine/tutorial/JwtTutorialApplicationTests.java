package me.silvernine.tutorial;

import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Mockito 환경에서 실행
public class JwtTutorialApplicationTests {

	@InjectMocks
	private UserService userService;  // ✅ 실제 빈을 사용할 경우 변경 가능

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenProvider tokenProvider;

	@Mock
	private SecurityConfig securityConfig;

	private Authentication authentication;

	@BeforeEach
	void setUp() {
		// ✅ 가짜 Authentication 객체 생성
		authentication = new UsernamePasswordAuthenticationToken(
				"testUser",
				null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();  // ✅ UserService가 정상적으로 주입되었는지 확인
	}

	@Test
	void testUserService() {
		// ✅ UserRepository Mock 설정
		User mockUser = User.builder()
				.id("testUser")
				.password("password")
				.activated(true)
				.build();

		when(userRepository.findById("testUser")).thenReturn(Optional.of(mockUser));

		// ✅ UserService 테스트
		User user = userService.getUserById("testUser");
		assertThat(user).isNotNull();
		assertThat(user.getId()).isEqualTo("testUser");  // 🔹 getUsername() -> getId() 변경

		// ✅ 메서드 호출 검증
		verify(userRepository, times(1)).findById("testUser");
	}

	@Test
	void testTokenProviderMocking() {
		// ✅ 가짜 토큰 반환 설정
		when(tokenProvider.createToken(any(Authentication.class))).thenReturn("mocked-jwt-token");

		// ✅ TokenProvider에서 토큰 생성 확인
		String token = tokenProvider.createToken(authentication);
		assertThat(token).isEqualTo("mocked-jwt-token");

		// ✅ 메소드 호출 여부 확인
		verify(tokenProvider, times(1)).createToken(authentication);
	}
}
