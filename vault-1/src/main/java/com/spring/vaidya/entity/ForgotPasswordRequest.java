package com.spring.vaidya.entity;

import io.swagger.v3.oas.annotations.media.Schema; // OpenAPI annotation

@Schema(description = "Request model for forgot password functionality")
public class ForgotPasswordRequest {

    @Schema(description = "User's registered email address", example = "user@example.com", required = true)
    private String email;

    // ✅ Default constructor
    public ForgotPasswordRequest() {
        super();
    }

    // ✅ Parameterized constructor
    public ForgotPasswordRequest(String email) {
        super();
        this.email = email;
    }

    // ✅ Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ✅ toString method for logging/debugging
    @Override
    public String toString() {
        return "ForgotPasswordRequest [email=" + email + "]";
    }
}
