package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> { // ✅ user_id가 PK

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByUserId(String userId); // ✅ PK인 user_id로 검색

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(String id);  // ✅ 사용자 입력 ID 기반 검색 추가
}
