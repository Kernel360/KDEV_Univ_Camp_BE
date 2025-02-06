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

    /**
     * ✅ 회원가입 기능
     */
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findByUserId(userDto.getId()).isPresent()) { // ✅ userId 필드 기준 중복 체크
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        User user = User.builder()
                .userId(userDto.getId()) // ✅ userId 사용
                .password(passwordEncoder.encode(userDto.getPassword())) // ✅ 비밀번호 암호화 저장
                .nickname(userDto.getNickname())
                .activated(true)
                .isAdmin(false)
                .build();

        userRepository.save(user);
        return new UserDto(user.getUserId(), userDto.getPassword(), user.getNickname());
    }

    /**
     * ✅ 특정 ID의 사용자 닉네임 가져오기
     */
    public String getUserNickname(String userId) {
        return userRepository.findByUserId(userId)
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    /**
     * ✅ 현재 로그인한 사용자 정보 가져오기
     */
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId()
                .flatMap(userRepository::findOneWithAuthoritiesByUserId)
                .map(user -> new UserDto(user.getUserId(), null, user.getNickname()))
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    /**
     * ✅ 특정 사용자 정보 가져오기 (관리자 전용)
     */
    public UserDto getUserWithAuthorities(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> new UserDto(user.getUserId(), null, user.getNickname()))
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
}
