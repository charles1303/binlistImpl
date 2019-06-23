package com.projects.binlist.services;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projects.binlist.dto.requests.SignUpRequest;
import com.projects.binlist.exceptions.GeneralException;
import com.projects.binlist.models.Role;
import com.projects.binlist.models.User;
import com.projects.binlist.repositories.UserRepository;
import com.projects.binlist.utils.RoleType;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    RoleService roleService;
    
    @Autowired
    PasswordEncoder passwordEncoder;
	
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

    public Optional<User> findByUsernameOrEmail(String username, String email) {
    	return userRepository.findByUsernameOrEmail(username, email);
    }

    public Optional<User> findByUsername(String username) {
    	return userRepository.findByUsername(username);
    }
    
    public User registerUser(SignUpRequest signUpRequest, RoleType roleType) throws GeneralException {
    	User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleService.findByRoleType(roleType)
                .orElseThrow(() -> new GeneralException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
    	return userRepository.save(user);
    }

}
