package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.AuthRequest;
import com.niraj.ecommerce.dto.AuthResponse;
import com.niraj.ecommerce.jwt.JwtUtil;
import com.niraj.ecommerce.model.Role;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // 1. REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {

        // Check if email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Email is already in use!"));
        }

        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        // CRITICAL: Encrypt the password before saving!
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        // Save to database
        userRepository.save(user);

        // Generate their VIP Wristband (JWT)
        String jwtToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(jwtToken, "Registration successful"));
    }

    // 2. LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // The AuthenticationManager checks the database and compares the encrypted passwords.
        // If the password is wrong, this line will throw an exception automatically.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // If we reach this line, the password was correct! Fetch the user.
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        // Generate a fresh JWT
        String jwtToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(jwtToken, "Login successful"));
    }
}