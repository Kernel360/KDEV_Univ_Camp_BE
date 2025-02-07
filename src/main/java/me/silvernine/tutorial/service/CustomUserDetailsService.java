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
      return userRepository.findById(username)
              .map(user -> createUser(username, user))
              .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
      if (!user.isActivated()) {
         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
      }

      // ✅ 권한 매핑 변경 (getAuthorityName() → getAuthority())
      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())) // 수정된 부분
              .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(
              user.getId(),
              user.getPassword(),
              grantedAuthorities
      );
   }
}
