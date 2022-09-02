package com.demo.player.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.player.models.UserSp;

public interface UserRepo extends JpaRepository<UserSp, Long> {
	Optional<UserSp> findUserById(Long uuid);
	@Query(value="SELECT * FROM user WHERE username = :username", nativeQuery = true)
	Optional<UserSp> findByUserName(String username);
	@Query(value="SELECT * FROM user WHERE email = :email", nativeQuery = true)
	Optional<UserSp> findByEmail(String email);
	@Query(value="SELECT user_id from USER WHERE username = :username", nativeQuery = true)
	Optional<Long> findIdByUsername(String username);
	@Query(value="SELECT * FROM user WHERE user_id = :userid", nativeQuery = true)
	Optional<UserSp> findUserSpByUserId(Long userid);
	
}	
