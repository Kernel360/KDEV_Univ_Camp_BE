package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      System.out.println("🔍 [DEBUG] CustomUserDetailsService.loadUserByUsername() 호출됨, username: " + username);

      return userRepository.findById(username)
              .map(user -> {
                 System.out.println("✅ [DEBUG] 사용자 조회 성공: " + user.getId());
                 return createUser(username, user);
              })
              .orElseThrow(() -> {
                 System.out.println("❌ [ERROR] 사용자 조회 실패: " + username);
                 return new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
              });
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
      System.out.println("🔍 [DEBUG] CustomUserDetailsService.createUser() 호출됨, username: " + username);

      if (!user.isActivated()) {
         System.out.println("❌ [ERROR] 비활성화된 계정: " + username);
         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
      }

      // ✅ 권한 매핑 변경 (디버깅 코드 추가)
      System.out.println("🔍 [DEBUG] 사용자 권한 조회 시작: " + username);
      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> {
                 System.out.println("✅ [DEBUG] 권한 매핑: " + authority.getAuthority());
                 return new SimpleGrantedAuthority(authority.getAuthority());
              })
              .collect(Collectors.toList());

      System.out.println("✅ [DEBUG] 최종 권한 리스트: " + grantedAuthorities);

      return new org.springframework.security.core.userdetails.User(
              user.getId(),
              user.getPassword(),
              grantedAuthorities
      );
   }
}
