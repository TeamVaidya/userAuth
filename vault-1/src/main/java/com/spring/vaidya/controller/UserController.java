package com.spring.vaidya.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.AuthRequest;
import com.spring.vaidya.entity.LoginRequest;
import com.spring.vaidya.entity.LoginResponse;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.AuthenticationFailedException;
import com.spring.vaidya.exception.UserNotFoundException;
import com.spring.vaidya.jwt.JwtUtils;
import com.spring.vaidya.repo.UserRepository;
import com.spring.vaidya.service.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@OpenAPIDefinition(info = @Info(title = "User API", version = "1.0", description = "APIs for managing users"))
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User Controller", description = "Endpoints for user authentication and management")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the given details")
    @PostMapping("/new")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            logger.info("Registering new user with email: {}", user.getUserEmail());
            userService.registerUser(user);
            logger.info("User registered successfully: {}", user.getUserEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            logger.error("User registration failed for email: {}. Error: {}", user.getUserEmail(), e.getMessage(), e);
            throw new RuntimeException("User registration failed", e);
        }
    }

    @Operation(summary = "Authenticate user", description = "Validates user credentials and returns a JWT token")
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            logger.info("Authenticating user: {}", authRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authRequest.getUsername());
            logger.info("User authenticated successfully: {}", authRequest.getUsername());
            return ResponseEntity.ok("Bearer " + jwt);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}. Error: {}", authRequest.getUsername(), e.getMessage(), e);
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }

    @Operation(summary = "Welcome message", description = "Returns a welcome message for API testing")
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        logger.info("Welcome endpoint accessed.");
        return ResponseEntity.ok("Welcome to the JWT-secured API!");
    }

    @Operation(summary = "Protected route", description = "Returns a message if the user is authenticated with a valid JWT")
    @GetMapping("/protected")
    public ResponseEntity<String> protectedRoute() {
        logger.info("Protected route accessed.");
        return ResponseEntity.ok("This is a protected route, only accessible with a valid JWT.");
    }

    @Operation(summary = "Doctor login", description = "Authenticates a doctor and returns a JWT token if successful")
    @PostMapping("/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest) {
        logger.info("Doctor login attempt for email: {}", loginRequest.getUserEmail());

        User doctor = userRepository.findByUserEmailIgnoreCase(loginRequest.getUserEmail())
                .orElseThrow(() -> {
                    logger.warn("Doctor not found with email: {}", loginRequest.getUserEmail());
                    return new UserNotFoundException("Doctor not found with email: " + loginRequest.getUserEmail());
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword())) {
            logger.warn("Invalid credentials for email: {}", loginRequest.getUserEmail());
            throw new AuthenticationFailedException("Invalid credentials");
        }

        if (!doctor.isEnabled()) {
            logger.warn("Doctor account is not verified: {}", loginRequest.getUserEmail());
            throw new AuthenticationFailedException("Doctor is not verified");
        }

        String jwt = jwtUtils.generateJwtToken(doctor.getUserEmail());
        logger.info("Doctor login successful: {}", loginRequest.getUserEmail());

        LoginResponse response = new LoginResponse(jwt, doctor.getFullName(), doctor.getUserId(),doctor.getRoleId());
        return ResponseEntity.ok(response);
    }
}
