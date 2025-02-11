package me.silvernine.tutorial;

import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTutorialApplicationTests {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;  // ✅ Repository는 MockBean 사용

	@MockBean
	private TokenProvider tokenProvider;  // ✅ JWT 관련 클래스는 MockBean 처리

	@MockBean
	private SecurityConfig securityConfig;

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();  // ✅ Bean 정상 주입 확인
	}
}
