package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 회원가입 기능 추가
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.existsById(userDto.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        User user = User.builder()
                .id(userDto.getId())
                .password(passwordEncoder.encode(userDto.getPassword())) // 비밀번호 암호화
                .nickname(userDto.getNickname())
                .activated(true) // 기본적으로 활성화된 계정으로 설정
                .admin(false) // 기본적으로 일반 사용자
                .build();

        userRepository.save(user);
        return UserDto.from(user, userDto.getPassword()); // 비밀번호 유지하여 반환
    }

    // ✅ 닉네임 가져오기 (String ID 기반)
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
