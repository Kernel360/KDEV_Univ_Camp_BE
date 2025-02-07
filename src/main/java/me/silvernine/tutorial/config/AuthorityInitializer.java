package me.silvernine.tutorial.config;

import me.silvernine.tutorial.entity.Authority;
import me.silvernine.tutorial.repository.AuthorityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthorityInitializer implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;

    public AuthorityInitializer(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) {
        if (authorityRepository.findById("ROLE_USER").isEmpty()) {
            Authority userAuthority = new Authority("ROLE_USER");
            authorityRepository.save(userAuthority);
        }

        if (authorityRepository.findById("ROLE_ADMIN").isEmpty()) {
            Authority adminAuthority = new Authority("ROLE_ADMIN");
            authorityRepository.save(adminAuthority);
        }
    }
}
