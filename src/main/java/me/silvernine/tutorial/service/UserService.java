package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ 회원가입 기능 (수정)
     */
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).isPresent()) { // ✅ id 필드 기준 중복 체크
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        User user = User.builder()
                .userId(UUID.randomUUID().toString()) // ✅ userId 자동 생성
                .id(userDto.getId()) // ✅ 사용자가 입력한 ID
                .password(passwordEncoder.encode(userDto.getPassword())) // ✅ 비밀번호 암호화 저장
                .nickname(userDto.getNickname())
                .activated(true) // ✅ 계정 활성화 기본값 true
                .isAdmin(false) // ✅ 기본적으로 일반 사용자
                .build();

        userRepository.save(user);
        return new UserDto(user.getId(), userDto.getPassword(), user.getNickname());
    }

    /**
     * ✅ 특정 ID의 사용자 닉네임 가져오기
     */
    public String getUserNickname(String id) {
        return userRepository.findById(id) // ✅ id 필드 기준 검색
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    /**
     * ✅ 현재 로그인한 사용자 정보 가져오기
     */
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId()
                .flatMap(userRepository::findOneWithAuthoritiesById) // ✅ 사용자가 입력한 ID 기준 검색
                .map(user -> new UserDto(user.getId(), null, user.getNickname()))
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    /**
     * ✅ 특정 사용자 정보 가져오기 (관리자 전용)
     */
    public UserDto getUserWithAuthorities(String id) {
        return userRepository.findOneWithAuthoritiesById(id) // ✅ id 필드 기준 검색
                .map(user -> new UserDto(user.getId(), null, user.getNickname())) // ✅ 비밀번호 제외 후 반환
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
}
