package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(String id);
}
