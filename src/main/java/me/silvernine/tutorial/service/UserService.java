package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.exception.NotFoundMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ 회원가입 기능 (비밀번호 암호화 후 저장)
     */
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userDto.getId() == null || userDto.getId().isEmpty()) {
            throw new IllegalArgumentException("ID는 반드시 입력해야 합니다.");
        }

        if (userRepository.existsById(userDto.getId())) { // 중복 체크
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        // ✅ 비밀번호 암호화 적용
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .userId(UUID.randomUUID().toString()) // userId 자동 생성
                .id(userDto.getId()) // 사용자가 입력한 ID
                .password(encryptedPassword) // ✅ 비밀번호 암호화 후 저장
                .nickname(userDto.getNickname())
                .activated(true) // 계정 활성화 기본값 true
                .isAdmin(false) // 기본적으로 일반 사용자
                .build();

        userRepository.save(user);

        // ✅ 비밀번호를 응답에서 제거한 UserDto 반환
        return UserDto.from(user);
    }

    /**
     * ✅ 로그인 시 비밀번호 검증 기능 (로그 추가)
     */
    public boolean validatePassword(String id, String rawPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));

        // ✅ 비밀번호 검증 로그 추가
        boolean isMatched = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("🚀 [비밀번호 검증] 입력값=" + rawPassword + ", DB저장값=" + user.getPassword() + ", 검증결과=" + isMatched);

        return isMatched;
    }

    /**
     * ✅ 특정 ID의 사용자 닉네임 가져오기
     */
    public String getUserNickname(String id) {
        return userRepository.findById(id)
                .map(User::getNickname)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    /**
     * ✅ 현재 로그인한 사용자 정보 가져오기
     */
    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId()
                .flatMap(userRepository::findById)
                .map(UserDto::from) // ✅ 비밀번호 제거된 DTO 반환
                .orElseThrow(() -> new NotFoundMemberException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    /**
     * ✅ 특정 사용자 정보 가져오기 (관리자 전용)
     */
    public UserDto getUserWithAuthorities(String id) {
        return userRepository.findById(id)
                .map(UserDto::from) // ✅ 비밀번호 제거된 DTO 반환
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
}
