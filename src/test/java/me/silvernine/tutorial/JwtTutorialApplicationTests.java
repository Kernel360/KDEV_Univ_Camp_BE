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

@ExtendWith(MockitoExtension.class)
public class JwtTutorialApplicationTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenProvider tokenProvider;

	@Mock
	private SecurityConfig securityConfig;

	private Authentication authentication;

	@BeforeEach
	void setUp() {
		authentication = new UsernamePasswordAuthenticationToken(
				"testUser",
				null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();
	}

	@Test
	void testUserService() {
		User mockUser = User.builder()
				.id("testUser")
				.password("password")
				.activated(true)
				.build();

		when(userRepository.findById("testUser")).thenReturn(Optional.of(mockUser));

		User user = userService.getUserById("testUser");
		assertThat(user).isNotNull();
		assertThat(user.getId()).isEqualTo("testUser");

		verify(userRepository, times(1)).findById("testUser");
	}

	@Test
	void testTokenProviderMocking() {
		when(tokenProvider.createToken(any(Authentication.class))).thenReturn("mocked-jwt-token");

		String token = tokenProvider.createToken(authentication);
		assertThat(token).isEqualTo("mocked-jwt-token");

		// ✅ 메소드 호출 여부 확인
		verify(tokenProvider, times(1)).createToken(authentication);
	}
}
