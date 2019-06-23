package com.projects.binlist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.binlist.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);
}
