package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> { // ✅ userId(UUID) 기준으로 관리

    /**
     * ✅ 사용자 입력 ID (id) 기준으로 사용자 정보 조회
     * @param id 사용자 입력 ID (ex: "admin", "user123")
     * @return Optional<User>
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(String id);

    /**
     * ✅ UUID 기반 userId 기준으로 사용자 정보 조회 (로그인 후 내부 시스템에서 사용)
     * @param userId UUID 기반의 userId
     * @return Optional<User>
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserId(String userId);

    /**
     * ✅ 사용자가 입력한 ID(id)가 이미 존재하는지 확인
     * @param id 사용자 입력 ID (ex: "admin", "user123")
     * @return boolean (true: 존재함, false: 없음)
     */
    boolean existsById(String id);
}
