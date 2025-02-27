package com.spring.vaidya.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.ResourceNotFoundException;
import com.spring.vaidya.repo.DoctorRepository;
import com.spring.vaidya.service.DoctorServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;

@OpenAPIDefinition(info = @Info(title = "Doctor API", version = "1.0", description = "APIs for managing doctors"))
@Tag(name = "Doctor Controller", description = "Operations related to doctor management")
@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:5173/")
public class DoctorController {

    private static final Logger logger = LogManager.getLogger(DoctorController.class);

    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Operation(summary = "Register a new doctor", description = "Creates a new doctor account.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody User doctor) {
        logger.info("Received request to register doctor with email: {}", doctor.getUserEmail());
        try {
            ResponseEntity<?> response = doctorService.saveDoctor(doctor);
            logger.info("Doctor registered successfully: {}", doctor.getUserEmail());
            return response;
        } catch (Exception e) {
            logger.error("Error registering doctor: {}", doctor.getUserEmail(), e);
            throw new RuntimeException("Failed to register doctor", e);
        }
    }

    @Operation(summary = "Confirm doctor account", description = "Confirms doctor email using a token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account confirmed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmDoctorAccount(@RequestParam("token") String confirmationToken) {
        logger.info("Received request to confirm doctor account with token: {}", confirmationToken);
        ResponseEntity<?> response = doctorService.confirmEmail(confirmationToken);
        logger.info("Doctor account confirmed with token: {}", confirmationToken);
        return response;
    }

    @Operation(summary = "Get all doctors", description = "Fetches a list of all registered doctors.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of doctors retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No doctors found", content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllDoctors() {
        logger.info("Fetching all registered doctors");
        List<User> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            logger.warn("No doctors found in the database");
            throw new ResourceNotFoundException("No doctors found");
        }
        logger.info("Successfully retrieved {} doctors", doctors.size());
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctor by ID", description = "Fetches a doctor's details by their unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping("/{doctorId}")
    public ResponseEntity<User> getDoctorById(@Parameter(description = "ID of the doctor to retrieve", required = true) @PathVariable Long doctorId) {
        logger.info("Fetching doctor details for ID: {}", doctorId);
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    logger.warn("Doctor not found with ID: {}", doctorId);
                    return new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
                });
        logger.info("Doctor details retrieved successfully for ID: {}", doctorId);
        return ResponseEntity.ok(doctor);
    }

    @Operation(summary = "Get doctor by email", description = "Fetches a doctor's details by their email.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping("/email")
    public ResponseEntity<User> getDoctorByEmail(@Parameter(description = "Email of the doctor to retrieve", required = true) @RequestParam String email) {
        logger.info("Fetching doctor details for email: {}", email);
        User doctor = doctorService.getDoctorByEmail(email);
        if (doctor == null) {
            logger.warn("Doctor not found with email: {}", email);
            throw new ResourceNotFoundException("Doctor not found with email: " + email);
        }
        logger.info("Doctor details retrieved successfully for email: {}", email);
        return ResponseEntity.ok(doctor);
    }
}
