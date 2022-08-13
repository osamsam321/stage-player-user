package com.demo.player.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.emums.RoleType;
import com.demo.player.models.Role;
import com.demo.player.models.UserSp;

public interface RoleRepo extends JpaRepository<Role, Long> {
	@Query(value="SELECT * FROM role WHERE role_type = :roleType", nativeQuery = true)
	Optional<Role> findByRoleName(String roleType);
}
