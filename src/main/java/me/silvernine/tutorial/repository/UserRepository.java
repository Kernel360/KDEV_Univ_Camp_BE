package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "authorities") // ✅ 권한 정보 함께 가져오기
    Optional<User> findOneWithAuthoritiesById(String id); // ✅ ID 기반 조회 메서드 추가

    boolean existsById(String id); // ✅ 회원가입 시 ID 중복 확인을 위한 메서드 추가
}
