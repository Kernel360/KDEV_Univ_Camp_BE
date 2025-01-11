package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findOneWithAuthoritiesByUsername(String username);

   // 추가: username이 존재하는지 확인하는 메서드
   boolean existsByUsername(String username);
}