package com.projects.binlist.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.binlist.models.Role;
import com.projects.binlist.repositories.RoleRepository;
import com.projects.binlist.utils.RoleType;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	public Optional<Role> findByRoleType(RoleType type) {
		return roleRepository.findByType(type);
	}

}
