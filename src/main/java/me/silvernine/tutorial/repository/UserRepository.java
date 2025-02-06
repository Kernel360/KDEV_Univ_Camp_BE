package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> { // ✅ id가 PK이므로 String 유지

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(String id); // ✅ id 기준 검색

    boolean existsById(String id); // ✅ id 필드 중복 체크

    Optional<User> findById(String id); // ✅ id 기준 검색
}
