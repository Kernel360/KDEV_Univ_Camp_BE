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
     * ✅ 비밀번호 검증 기능 (로그 추가)
     */
    public boolean validatePassword(String id, String rawPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("해당 ID의 사용자를 찾을 수 없습니다."));

        boolean isMatched = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("🚀 [비밀번호 검증] 입력값=" + rawPassword + ", DB저장값=" + user.getPassword() + ", 검증결과=" + isMatched);

        return isMatched;
    }
}
