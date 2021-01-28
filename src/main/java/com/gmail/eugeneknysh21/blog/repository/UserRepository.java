package com.gmail.eugeneknysh21.blog.repository;

import com.gmail.eugeneknysh21.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByAlias(String alias);
}
