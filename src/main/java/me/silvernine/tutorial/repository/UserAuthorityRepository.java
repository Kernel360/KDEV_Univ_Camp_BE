package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
