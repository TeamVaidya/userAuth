package com.spring.vaidya.entity;

import io.swagger.v3.oas.annotations.media.Schema; // OpenAPI annotation

@Schema(description = "Request model for user login")
public class LoginRequest {

    @Schema(description = "User's email address", example = "user@example.com")
    private String userEmail;

    @Schema(description = "User's password", example = "SecurePassword123")
    private String password;

    // ✅ Default constructor
    public LoginRequest() {
        super();
    }

    // ✅ Parameterized constructor
    public LoginRequest(String userEmail, String password) {
        super();
        this.userEmail = userEmail;
        this.password = password;
    }

    // ✅ Getters and Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ Override toString for better logging/debugging
    @Override
    public String toString() {
        return "LoginRequest [userEmail=" + userEmail + ", password=PROTECTED]";
    }
    
    
}
