package com.fooddelivery.userservice.service;

import com.fooddelivery.userservice.dto.AuthResponse;
import com.fooddelivery.userservice.dto.LoginRequest;
import com.fooddelivery.userservice.dto.RegisterRequest;
import com.fooddelivery.userservice.entity.Role;
import com.fooddelivery.userservice.entity.User;
import com.fooddelivery.userservice.repository.UserRepository;
import com.fooddelivery.userservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = request.getRoles() != null && !request.getRoles().isEmpty()
                ? request.getRoles()
                : new HashSet<>();
        if (roles.isEmpty()) {
            roles.add(Role.CUSTOMER);
        }
        user.setRoles(roles);

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved);
        return new AuthResponse(token, saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRoles());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRoles());
    }
}

