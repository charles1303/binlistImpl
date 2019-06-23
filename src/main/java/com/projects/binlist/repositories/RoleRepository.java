package com.projects.binlist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.binlist.models.Role;
import com.projects.binlist.utils.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByType(RoleType type);

}
