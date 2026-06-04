package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.AuthResponse;
import com.niraj.ecommerce.dto.RegisterRequest;
import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.LoginRequest;
import com.niraj.ecommerce.exception.ResourceAlreadyExistsException;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Role;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.repository.UserRepository;
import com.niraj.ecommerce.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setEmail(user.getEmail());
        authResponse.setMobile(user.getMobile());
        return new ApiResponse<AuthResponse>(true, "Registration successful", authResponse);
    }

    public ApiResponse<AuthResponse> login(LoginRequest request) {
        log.info("Attempting login for user with email: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> {
                    log.warn("Login failed - user not found with email: {}", request.getEmail());
                    return new ResourceNotFoundException("User not found!");
                });

        String jwtToken = jwtUtil.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setEmail(user.getEmail());
        authResponse.setMobile(user.getMobile());


        log.info("User logged in successfully: {}", request.getEmail());
        return new ApiResponse<AuthResponse>(true, "Login successful", authResponse);
    }
}