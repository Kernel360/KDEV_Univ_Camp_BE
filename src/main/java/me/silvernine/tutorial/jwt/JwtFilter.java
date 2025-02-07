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
@Component  // ✅ @Component 추가하여 Spring Bean으로 등록
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class); //  로그 추가

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
        String jwt = resolveToken(httpServletRequest); // 요청에서 JWT 추출
        String requestURI = httpServletRequest.getRequestURI(); // 요청 URI 추가

        // ✅ 인증 제외 URL인지 확인
        if (EXCLUDED_URLS.stream().anyMatch(requestURI::startsWith)) {
            log.debug("✅ 인증 제외 URL 접근: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 인증 정보 생성
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 저장
            log.debug("✅ SecurityContext에 '{}' 인증 정보를 저장했습니다. 요청 URI: {}", jwt, requestURI);
        } else {
            log.error("❌ Invalid JWT token: {}, 요청 URI: {}", jwt, requestURI); //  추가: JWT 검증 실패 로그
        }

        chain.doFilter(request, response); // 다음 필터로 요청 전달
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
