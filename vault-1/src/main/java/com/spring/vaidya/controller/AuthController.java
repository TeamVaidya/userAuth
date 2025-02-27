package com.spring.vaidya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.vaidya.entity.ErrorResponse;
import com.spring.vaidya.entity.ForgotPasswordRequest;
import com.spring.vaidya.entity.ResetPasswordRequest;
import com.spring.vaidya.service.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * Controller for handling authentication-related operations such as 
 * forgot password and reset password.
 */
@RestController
@RequestMapping("/auth")
@OpenAPIDefinition (info=@io.swagger.v3.oas.annotations.info.Info(title="Vaidya Authentication API", description="APIs for handling authentication-related operations such as password recovery."))
@Tag(name = "Authentication", description = "APIs for authentication-related operations such as password recovery")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Handles the "Forgot Password" functionality.
     * 
     * @param request Contains the user's email for password reset initiation.
     * @return ResponseEntity indicating success or failure.
     */
    @Operation(
            summary = "Forgot Password",
            description = "Initiates a password reset process by sending a reset link to the user's email.",
            requestBody = @RequestBody(
                description = "User email for password reset",
                required = true,
                content = @Content(schema = @Schema(implementation = ForgotPasswordRequest.class))
            ),
            responses = {
                @ApiResponse(responseCode = "200", description = "Password reset email sent successfully."),
                @ApiResponse(responseCode = "400", description = "Invalid email provided", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            }
        )
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        // Call the UserService to initiate the forgot password process.
        Object response = userService.initiateForgotPassword(request.getEmail());

        // If the response is an error, return an appropriate HTTP status code.
        if (response instanceof ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse) response;
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // If successful, return a success message.
        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    /**
     * Handles the "Reset Password" functionality.
     * 
     * @param request Contains the reset token and the new password.
     * @return ResponseEntity indicating success or failure.
     */
    @Operation(
            summary = "Reset Password",
            description = "Resets the user's password using the provided reset token.",
            requestBody = @RequestBody(
                description = "Reset token and new password",
                required = true,
                content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))
            ),
            responses = {
                @ApiResponse(responseCode = "200", description = "Password reset successful."),
                @ApiResponse(responseCode = "400", description = "Invalid or expired token", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            }
        )
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        // Call the UserService to reset the password using the provided token and new password.
        Object response = userService.resetPassword(request.getToken(), request.getNewPassword());

        // If the response is an error, return an appropriate HTTP status code.
        if (response instanceof ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse) response;
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // If successful, return a success message.
        return ResponseEntity.ok("Password reset successful.");
    }
}
