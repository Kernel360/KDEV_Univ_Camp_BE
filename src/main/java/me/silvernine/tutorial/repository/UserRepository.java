package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // 🔥 변경: userId가 PK이므로 Long으로 설정

    @EntityGraph(attributePaths = "authorities") // ✅ 권한 정보 함께 가져오기
    Optional<User> findOneWithAuthoritiesById(String id);

    boolean existsById(String id); // 🔥 추가: id 중복 체크
}
