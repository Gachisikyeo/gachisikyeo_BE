package com.example.gachisikyeo_be.global.users.repository;

import com.example.gachisikyeo_be.global.users.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
