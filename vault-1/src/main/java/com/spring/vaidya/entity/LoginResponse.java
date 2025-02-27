package com.spring.vaidya.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response model for user login")
public class LoginResponse {
    
    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Full name of the logged-in user", example = "John Doe")
    private String fullName;

    @Schema(description = "User ID of the logged-in user", example = "101")
    private Long userId;

    public LoginResponse(String token, String fullName, Long userId) {
        this.token = token;
        this.fullName = fullName;
        this.userId = userId;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "LoginResponse [token=" + token + ", fullName=" + fullName + ", userId=" + userId + "]";
	}
    
    
}
