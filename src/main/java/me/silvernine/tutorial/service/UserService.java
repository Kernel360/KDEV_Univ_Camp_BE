package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.Authority;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.entity.UserAuthority;
import me.silvernine.tutorial.repository.AuthorityRepository;
import me.silvernine.tutorial.repository.UserAuthorityRepository;
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
    private final UserAuthorityRepository userAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserAuthorityRepository userAuthorityRepository,
                       AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ 회원가입 기능 (기존 기능 유지 + 권한 저장 추가)
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

        // ✅ User 엔티티 생성
        User user = User.builder()
                .userId(UUID.randomUUID().toString()) // userId 자동 생성
                .id(userDto.getId()) // 사용자가 입력한 ID
                .password(encryptedPassword) // ✅ 비밀번호 암호화 후 저장
                .nickname(userDto.getNickname())
                .activated(true) // 계정 활성화 기본값 true
                .isAdmin(false) // 기본적으로 일반 사용자
                .build();

        // ✅ 유저 저장
        userRepository.save(user);

        // ✅ 기본 권한(ROLE_USER) 추가
        Authority authority = authorityRepository.findById("ROLE_USER")
                .orElseGet(() -> {
                    Authority newAuthority = new Authority("ROLE_USER");
                    return authorityRepository.save(newAuthority);
                });

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        // ✅ user_authority 테이블에 저장
        userAuthorityRepository.save(userAuthority);

        // ✅ 비밀번호를 응답에서 제거한 UserDto 반환
        return UserDto.from(user);
    }

    /**
     * ✅ 로그인 시 비밀번호 검증 기능 추가
     */
    public boolean validatePassword(String id, String rawPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));

        // ✅ 로그인할 때 암호화된 비밀번호와 비교
        return passwordEncoder.matches(rawPassword, user.getPassword());
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
