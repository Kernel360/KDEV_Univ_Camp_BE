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

        // ✅ ID가 일반 ID인지 UUID인지 구분하여 조회 (이중 조회 방지)
        User user = userRepository.findById(username)
                .or(() -> userRepository.findByUserId(username))
                .orElseThrow(() -> {
                    System.out.println("❌ [ERROR] 사용자 조회 실패: " + username);
                    return new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
                });

        System.out.println("✅ [DEBUG] 사용자 조회 성공: ID = " + user.getId() + ", UUID = " + user.getUserId());

        return createUser(user);
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        System.out.println("🔍 [DEBUG] CustomUserDetailsService.createUser() 호출됨, 사용자 ID: " + user.getId());

        if (!user.isActivated()) {
            System.out.println("❌ [ERROR] 비활성화된 계정: " + user.getId());
            throw new RuntimeException(user.getId() + " -> 활성화되어 있지 않습니다.");
        }

        // ✅ 권한 매핑 개선
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

        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}
