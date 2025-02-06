package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        if (userRepository.findById(userDto.getId()).isPresent()) { // ✅ 사용자 입력 ID 중복 체크
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        User user = User.builder()
                .id(userDto.getId()) // ✅ 사용자 입력 ID
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
