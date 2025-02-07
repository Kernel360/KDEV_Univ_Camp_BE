package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;  // ✅ 추가
import java.util.Optional;

@Repository  // ✅ Spring이 인식할 수 있도록 추가
public interface UserRepository extends JpaRepository<User, String> { // ✅ id(String) 기준으로 검색

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(String id); // ✅ 사용자가 입력한 ID 기준 조회
}
