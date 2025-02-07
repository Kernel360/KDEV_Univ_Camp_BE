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
	private UserService userService;  // ✅ 기존 @MockBean -> @Autowired로 변경

	@Autowired
	private UserRepository userRepository;  // ✅ 기존 @MockBean -> @Autowired로 변경

	@MockBean
	private TokenProvider tokenProvider;  // ✅ 기존 @MockBean -> @Autowired로 변경

	@Autowired
	private SecurityConfig securityConfig;  // ✅ 기존 @MockBean -> @Autowired로 변경

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();  // ✅ Bean 정상 주입 확인
	}
}
