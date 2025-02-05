package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 닉네임 가져오기 (String ID 기반)
    public String getUserNickname(String id) {
        return userRepository.findById(id)
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    // ✅ 현재 로그인한 사용자 정보 가져오기
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId()
                .flatMap(userRepository::findById)
                .map(UserDto::from)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    // ✅ 특정 사용자 정보 가져오기 (관리자 전용)
    public UserDto getUserWithAuthorities(String id) {
        return userRepository.findById(id)
                .map(UserDto::from)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
}
