package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   @EntityGraph(attributePaths = "authorities") // 권한 정보를 함께 로드
   Optional<User> findOneWithAuthoritiesByUsername(String username);

   boolean existsByUsername(String username); // 중복 확인 메서드
}
