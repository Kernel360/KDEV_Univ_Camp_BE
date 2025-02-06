package me.silvernine.tutorial.service;

import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String userId) { // ✅ userId 기준으로 변경
      return userRepository.findOneWithAuthoritiesByUserId(userId) // ✅ 수정됨
              .map(this::createUser)
              .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(User user) {
      if (!user.isActivated()) {
         throw new RuntimeException(user.getUserId() + " -> 활성화되어 있지 않습니다.");
      }

      Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toSet());

      return new org.springframework.security.core.userdetails.User(
              user.getUserId(), // ✅ userId 기준으로 수정
              user.getPassword(),
              grantedAuthorities
      );
   }
}
