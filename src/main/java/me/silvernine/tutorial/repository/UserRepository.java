package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> { // ✅ id(String) 기준으로 검색

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(String id); // ✅ 사용자가 입력한 ID 기준 조회
}
