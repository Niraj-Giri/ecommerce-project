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
import org.springframework.stereotype.Service;

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
        if (userRepository.existsByEmail(request.getEmail())) {
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
        AuthResponse authResponse = new AuthResponse(jwtToken);
        return new ApiResponse<AuthResponse>(true, "Registration successful", authResponse);
    }

    public ApiResponse<AuthResponse> login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        String jwtToken = jwtUtil.generateToken(user);
        AuthResponse authResponse = new AuthResponse(jwtToken);
        return new ApiResponse<AuthResponse>(true, "Login successful", authResponse);
    }
}