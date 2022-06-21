package com.demo.player.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.player.models.User;

public interface UserRepo extends JpaRepository<User, UUID> {
	Optional<User> findUserById(UUID uuid);
	@Query(value="SELECT * FROM user WHERE username = :username", nativeQuery = true)
	Optional<User> findByUserName(String username);
	@Query(value="SELECT * FROM user WHERE email = :email", nativeQuery = true)
	Optional<User> findByEmail(String email);
	
}	
