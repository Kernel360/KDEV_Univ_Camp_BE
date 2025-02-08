package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(String id);  // ✅ id(문자열) 기준으로 검색하도록 유지!

    // 🚨 UUID가 아닌 id(문자열) 기준으로 검색하는 메서드를 확실하게 추가
    Optional<User> findByIdEquals(String id);
}
