package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.User;
import me.silvernine.tutorial.repository.UserRepository;
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

    // 닉네임 가져오기 (String ID 기반)
    public String getUserNickname(String id) {
        return userRepository.findById(id)
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 닉네임을 찾을 수 없습니다."));
    }
}
