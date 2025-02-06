package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.Authority;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
     * ✅ 회원가입 기능 (ROLE_USER 권한 추가, ID 필수 입력 및 중복 체크 강화)
     */
    @Transactional
    public UserDto signup(UserDto userDto) {
        // ✅ 사용자 입력 ID가 null이거나 공백이면 예외 발생
        if (userDto.getId() == null || userDto.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 ID는 필수 입력값입니다.");
        }

        // ✅ 중복 체크 (ID가 이미 존재하는 경우 예외 발생)
        if (userRepository.findById(userDto.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        User user = User.builder()
                .id(userDto.getId()) // ✅ 사용자 입력 ID
                .password(passwordEncoder.encode(userDto.getPassword())) // ✅ 비밀번호 암호화 저장
                .nickname(userDto.getNickname())
                .activated(true) // ✅ 계정 활성화 기본값 true
                .isAdmin(false) // ✅ 기본적으로 일반 사용자
                .build();

        // ✅ 기본 권한 부여 (ROLE_USER)
        Authority userAuthority = new Authority("ROLE_USER");
        user.setAuthorities(Collections.singleton(userAuthority));

        userRepository.save(user);
        return new UserDto(user.getId(), userDto.getPassword(), user.getNickname());
    }

    /**
     * ✅ 특정 ID의 사용자 닉네임 가져오기
     */
    public String getUserNickname(String id) {
        return userRepository.findById(id) // ✅ 사용자 입력 ID로 검색
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    /**
     * ✅ 현재 로그인한 사용자 정보 가져오기 (id 기반 검색)
     */
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId() // ✅ 현재 로그인한 ID 가져오기
                .flatMap(userRepository::findById) // ✅ 사용자 입력 ID로 검색
                .map(user -> new UserDto(user.getId(), null, user.getNickname())) // ✅ 비밀번호 제외 후 반환
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    /**
     * ✅ 특정 사용자 정보 가져오기 (관리자 전용)
     */
    public UserDto getUserWithAuthorities(String id) {
        return userRepository.findById(id) // ✅ 사용자 입력 ID로 검색
                .map(user -> new UserDto(user.getId(), null, user.getNickname())) // ✅ 비밀번호 제외 후 반환
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    /**
     * ✅ 로그인 시 ID로 사용자 검색 (기존 코드에서 빠졌던 부분 추가)
     */
    @Transactional
    public Optional<User> findByLoginId(String id) {
        return userRepository.findById(id);
    }
}
