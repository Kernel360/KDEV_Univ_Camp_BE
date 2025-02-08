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

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ✅ PasswordEncoder 추가

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        System.err.println("🚀 [DEBUG] UserDetailsService.loadUserByUsername() 호출됨, username: " + username);

        User user = userRepository.findByIdEquals(username)
                .orElseThrow(() -> {
                    System.err.println("❌ [ERROR] User not found for username: " + username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });

        System.err.println("✅ [DEBUG] UserDetails 로드 성공! userId: " + user.getUserId());

        return createUser(user);
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        System.out.println("🔍 [DEBUG] CustomUserDetailsService.createUser() 호출됨, 사용자 ID: " + user.getId());

        if (!user.isActivated()) {
            System.out.println("❌ [ERROR] 비활성화된 계정: " + user.getId());
            throw new RuntimeException(user.getId() + " -> 활성화되어 있지 않습니다.");
        }

        System.out.println("🔍 [DEBUG] 사용자 권한 조회 시작: " + user.getId());
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> {
                    System.out.println("✅ [DEBUG] 권한 매핑: " + authority.getAuthority());
                    return new SimpleGrantedAuthority(authority.getAuthority());
                })
                .collect(Collectors.toList());

        if (grantedAuthorities.isEmpty()) {
            System.out.println("⚠️ 사용자 권한이 없어서 기본 권한 추가 (ROLE_USER)");
            grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        System.out.println("✅ [DEBUG] 최종 권한 리스트: " + grantedAuthorities);

        // ✅ 비밀번호 디버깅 추가
        System.err.println("🔍 [DEBUG] loadUserByUsername() 반환하는 UserDetails:");
        System.err.println("🔍 ID: " + user.getId());
        System.err.println("🔍 Password: " + user.getPassword());
        System.err.println("🔍 Password Matches: " + passwordEncoder.matches("123", user.getPassword())); // ✅ 디버깅 추가

        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(), // ✅ 여기서 암호화된 비밀번호가 들어가는지 확인!
                grantedAuthorities
        );
    }
}

