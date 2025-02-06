package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> { // ✅ userId가 String이므로 변경

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(String id);

    boolean existsById(String id); // ✅ id 필드 중복 체크

    Optional<User> findByUserId(String userId); // ✅ userId(PK) 기준 조회
}
