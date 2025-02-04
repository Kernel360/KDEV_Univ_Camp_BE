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

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String id) {  // ✅ username → id 변경
      return userRepository.findOneWithAuthoritiesById(id)  // ✅ findOneWithAuthoritiesByUsername → findOneWithAuthoritiesById 변경
              .map(this::createUser)
              .orElseThrow(() -> new UsernameNotFoundException(id + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(User user) {
      if (!user.isActivated()) {
         throw new RuntimeException(user.getId() + " -> 활성화되어 있지 않습니다."); // ✅ username → id 변경
      }

      // ✅ isAdmin 값에 따라 권한 설정
      List<GrantedAuthority> grantedAuthorities = user.isAdmin()
              ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
              : Collections.emptyList();

      return new org.springframework.security.core.userdetails.User(
              user.getId(),  // ✅ username → id 변경
              user.getPassword(),
              grantedAuthorities
      );
   }
}
