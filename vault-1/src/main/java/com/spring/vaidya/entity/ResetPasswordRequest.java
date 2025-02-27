package com.spring.vaidya.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Request object for resetting user password")
public class ResetPasswordRequest {

    @Schema(description = "Password reset token", example = "5f2d3a4b-8d9e-4c1a-b3f7-99d9c9a4d9a1", required = true)
    private String token;

    @Schema(description = "New password for the user", example = "NewP@ssw0rd!", required = true)
    private String newPassword;

    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest [token=" + token + ", newPassword=" + newPassword + "]";
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	
    
    
}
