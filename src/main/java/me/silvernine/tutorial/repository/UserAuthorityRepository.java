package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    boolean existsByUserUserId(String userId);
}
