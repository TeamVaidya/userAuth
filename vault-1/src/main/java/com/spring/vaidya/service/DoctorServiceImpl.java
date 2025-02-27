package com.spring.vaidya.service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.ConfirmTokenDoctor;
import com.spring.vaidya.entity.ErrorResponse;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.repo.ConfirmTokenDoctorRepo;
import com.spring.vaidya.repo.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConfirmTokenDoctorRepo confirmTokenDoctorRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String AADHAAR_REGEX = "^[0-9]{12}$";
    private static final String PHONE_REGEX = "\\d{10}";

    @Override
    public ResponseEntity<ErrorResponse> saveDoctor(User doctor) {
        logger.info("Attempting to register doctor with email: {}", doctor.getUserEmail());

        if (!Pattern.matches(EMAIL_REGEX, doctor.getUserEmail())) {
            logger.warn("Invalid email format: {}", doctor.getUserEmail());
            return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), 400, "INVALID_EMAIL", "Invalid email format!"));
        }

        if (!Pattern.matches(AADHAAR_REGEX, doctor.getAadharNo())) {
            logger.warn("Invalid Aadhaar number: {}", doctor.getAadharNo());
            return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), 400, "INVALID_AADHAAR", "Aadhaar number must be exactly 12 digits!"));
        }

        if (!doctor.getPhoneNumber().matches(PHONE_REGEX)) {
            logger.warn("Invalid phone number: {}", doctor.getPhoneNumber());
            return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), 400, "INVALID_PHONE", "Phone number must be exactly 10 digits!"));
        }

        if (doctorRepository.existsByUserEmail(doctor.getUserEmail())) {
            logger.warn("Email already exists: {}", doctor.getUserEmail());
            return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), 400, "EMAIL_EXISTS", "Error: Email is already in use!"));
        }

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);
        logger.info("Doctor registered successfully: {}", doctor.getUserEmail());

        ConfirmTokenDoctor confirmationToken = new ConfirmTokenDoctor(doctor);
        confirmTokenDoctorRepo.save(confirmationToken);
        logger.info("Confirmation token generated for doctor: {}", doctor.getUserEmail());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(doctor.getUserEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "https://vault1-production-7c73.up.railway.app/doctor/confirm-account?token=" 
                + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);
        logger.info("Verification email sent to: {}", doctor.getUserEmail());

        return ResponseEntity.ok(new ErrorResponse(LocalDateTime.now(), 200, "VERIFICATION_EMAIL_SENT", 
            "Verify email by the link sent to your email address"));
    }

    @Override
    public ResponseEntity<ErrorResponse> confirmEmail(String confirmationToken) {
        logger.info("Attempting to confirm email with token: {}", confirmationToken);
        ConfirmTokenDoctor token = confirmTokenDoctorRepo.findByConfirmTokenDoctor(confirmationToken);

        if (token != null) {
            User doctor = doctorRepository.findByUserEmailIgnoreCase(token.getDoctorEntity().getUserEmail());
            doctor.setEnabled(true);
            doctorRepository.save(doctor);
            logger.info("Doctor email verified successfully: {}", doctor.getUserEmail());
            return ResponseEntity.ok(new ErrorResponse(LocalDateTime.now(), 200, "EMAIL_VERIFIED", 
                "Email verified successfully!"));
        }

        logger.warn("Invalid confirmation token: {}", confirmationToken);
        return ResponseEntity.badRequest().body(
            new ErrorResponse(LocalDateTime.now(), 400, "INVALID_TOKEN", "Error: Couldn't verify email"));
    }

    @Override
    public User getDoctorByEmail(String email) {
        logger.info("Fetching doctor details for email: {}", email);
        return doctorRepository.findByUserEmail(email);
    }
}
