package me.silvernine.tutorial.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.exception.DuplicateMemberException;
import me.silvernine.tutorial.exception.NotFoundMemberException;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 서비스
     */
    @Transactional
    public UserDto signup(UserDto userDto) {
        // 중복 회원 확인
        if (userRepository.existsById(userDto.getId())) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        String rawPassword = userDto.getPassword(); // ✅ 원본 비밀번호 저장

        User user = User.builder()
                .id(userDto.getId())
                .password(passwordEncoder.encode(userDto.getPassword())) // ✅ 비밀번호 암호화 후 저장
                .nickname(userDto.getNickname())
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user), rawPassword); // ✅ 원본 비밀번호 포함
    }

    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId() // ✅ username → id로 변경
                .flatMap(userRepository::findOneWithAuthoritiesById)
                .map(user -> UserDto.from(user, null)) // ✅ 원본 비밀번호는 반환하지 않음
                .orElseThrow(() -> new NotFoundMemberException("현재 로그인한 사용자를 찾을 수 없습니다."));
    }

    /**
     * 특정 유저 정보 가져오기 (관리자 전용)
     */
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String Id) {
        return null;
    }

    public String getUserNickname(String id) {
        return userRepository.findById(Long.parseLong(id)) // String → Long 변환
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }
}