package me.silvernine.tutorial.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 */
@Component
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    // ✅ JWT 인증을 제외할 엔드포인트 목록
    private static final List<String> EXCLUDED_URLS = List.of(
            "/swagger-ui/",
            "/swagger-ui.html",
            "/swagger-resources/",
            "/v3/api-docs/",
            "/webjars/",
            "/h2-console/",
            "/api/signup",
            "/api/authenticate",
            "/api/auth-header-check",
            "/api/user"
    );

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI(); // 요청 URI
        String jwt = resolveToken(httpServletRequest); // 요청에서 JWT 추출

        System.out.println("🔍 [JWT 필터] 요청 URI: " + requestURI);
        System.out.println("🔍 [JWT 필터] 추출된 토큰: " + jwt);

        // ✅ 인증 제외 URL인지 확인
        if (EXCLUDED_URLS.stream().anyMatch(requestURI::startsWith)) {
            System.out.println("✅ 인증 제외 URL 접근: " + requestURI);
            chain.doFilter(request, response);
            return;
        }

        // ✅ JWT 검증
        boolean isValid = tokenProvider.validateToken(jwt);
        System.out.println("🔍 [JWT 필터] JWT 유효성 검사 결과: " + isValid);

        if (!StringUtils.hasText(jwt) || !isValid) { // JWT 검증 실패 시 초기화
            System.out.println("❌ [JWT 필터] JWT 검증 실패로 SecurityContext 초기화.");
            SecurityContextHolder.clearContext();
        } else {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ SecurityContextHolder에 저장된 인증 정보 확인
            Authentication storedAuth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("✅ [JWT 필터] SecurityContext에 저장된 인증 정보: " + storedAuth);

            System.out.println("✅ [JWT 필터] SecurityContext에 인증 정보 저장 완료. 요청 URI: " + requestURI);
        }

        chain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // ✅ 디버깅 로그 추가
        System.out.println("🔍 [JWT 필터] 요청 URI: " + request.getRequestURI());
        System.out.println("🔍 [JWT 필터] Authorization 헤더 값: " + bearerToken);

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            System.out.println("❌ [JWT 필터] Authorization 헤더가 없거나 형식이 잘못됨.");
            return null;
        }

        System.out.println("✅ [JWT 필터] Bearer 토큰 감지됨");
        return bearerToken.substring(7); // "Bearer " 이후의 값 추출
    }
}
