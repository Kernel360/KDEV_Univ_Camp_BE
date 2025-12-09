package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.Authority;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.entity.UserAuthority;
import me.silvernine.tutorial.repository.AuthorityRepository;
import me.silvernine.tutorial.repository.UserAuthorityRepository;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.exception.NotFoundMemberException;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserAuthorityRepository userAuthorityRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userDto.getId() == null || userDto.getId().isEmpty()) {
            throw new IllegalArgumentException("ID는 반드시 입력해야 합니다.");
        }

        if (userRepository.existsById(userDto.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        String generatedUserUUID = UUID.randomUUID().toString();

        User user = User.builder()
                .userId(generatedUserUUID)
                .id(userDto.getId())
                .password(encryptedPassword)
                .nickname(userDto.getNickname())
                .activated(true)
                .isAdmin(false)
                .build();

        userRepository.save(user);
        System.out.println("회원가입 성공: user_id(UUID) = " + user.getUserId());

        Authority authority = authorityRepository.findById("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(new Authority("ROLE_USER")));

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        userAuthorityRepository.save(userAuthority);
        System.out.println("user_authority 저장 완료: user_id(UUID) = " + user.getUserId());

        return UserDto.from(user);
    }

    public boolean validatePassword(String id, String rawPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));

        boolean isValid = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("비밀번호 검증: 입력값=" + rawPassword + ", DB 저장값=" + user.getPassword() + ", 검증결과=" + isValid);
        return isValid;
    }

    public String getUserNickname(String id) {
        return userRepository.findById(id)
                .map(User::getNickname)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }

    public UserDto getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentId()
                .flatMap(userRepository::findById)
                .map(UserDto::from)
                .orElseThrow(() -> new NotFoundMemberException("로그인한 사용자의 정보를 찾을 수 없습니다."));
    }

    public UserDto getUserWithAuthorities(String id) {
        return userRepository.findById(id)
                .map(UserDto::from)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
