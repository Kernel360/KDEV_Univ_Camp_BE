package me.silvernine.tutorial;

import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTutorialApplication.class)  // ✅ 명확한 설정 추가
public class JwtTutorialApplicationTests {

	@MockBean
	private UserService userService;  // ✅ 필요한 Bean을 Mocking

	@MockBean
	private TokenProvider tokenProvider;  // ✅ JWT 관련 Bean Mocking

	@MockBean
	private SecurityConfig securityConfig;  // ✅ 보안 설정 Mocking

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();  // ✅ Bean 정상 주입 확인
	}
}
