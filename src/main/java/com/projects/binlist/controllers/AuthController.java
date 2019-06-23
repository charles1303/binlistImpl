package com.projects.binlist.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projects.binlist.components.JwtManager;
import com.projects.binlist.dto.requests.LoginRequest;
import com.projects.binlist.dto.requests.SignUpRequest;
import com.projects.binlist.dto.responses.ApiResponse;
import com.projects.binlist.dto.responses.JwtAuthenticationResponse;
import com.projects.binlist.exceptions.GeneralException;
import com.projects.binlist.services.UserService;
import com.projects.binlist.utils.RoleType;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
    UserService userService;
    
    @Autowired
    JwtManager jwtManager;
    
    @Autowired
    AuthenticationManager authenticationManager;


	
	@PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws GeneralException {
		
		userService.registerUser(signUpRequest, RoleType.ROLE_USER);
        
        return ResponseEntity.ok(new ApiResponse(0, "User registered successfully"));
    }
	
	@PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) throws GeneralException {
       
        userService.registerUser(signUpRequest, RoleType.ROLE_ADMIN);
        
        return ResponseEntity.ok(new ApiResponse(0, "Admin registered successfully"));
    }
	
	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtManager.generateToken(authentication);
        return ResponseEntity.ok(new ApiResponse(0, "Token generated successfully",new JwtAuthenticationResponse(jwt)));
    }

}
