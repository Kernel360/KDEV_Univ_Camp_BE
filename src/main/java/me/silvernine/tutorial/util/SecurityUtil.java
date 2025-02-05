package me.silvernine.tutorial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

   private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

   private SecurityUtil() {}

   // ✅ 기존 getCurrentUsername()을 getCurrentId()로 변경
   public static Optional<String> getCurrentId() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         logger.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      String id = null;  // ✅ username → id 변경
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         id = springSecurityUser.getUsername();  // ✅ id를 가져옴
      } else if (authentication.getPrincipal() instanceof String) {
         id = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(id);
   }

   // ✅ `getCurrentUserId()`를 `getCurrentId()`를 활용하여 구현
   public static String getCurrentUserId() {
      return getCurrentId().orElseThrow(() ->
              new IllegalArgumentException("현재 인증된 사용자의 ID를 찾을 수 없습니다."));
   }
}
