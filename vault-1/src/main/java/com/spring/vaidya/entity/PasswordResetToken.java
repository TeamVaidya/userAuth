package com.spring.vaidya.entity;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Schema(description = "Entity representing a password reset token for users")
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the password reset token", example = "1")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique password reset token", example = "5f2d3a4b-8d9e-4c1a-b3f7-99d9c9a4d9a1")
    private String token;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Associated user for password reset")
    private User user;
    
    @Schema(description = "Expiration date and time of the token", example = "2024-09-30T15:30:00")
    private LocalDateTime expiryDate;

    /**
     * Constructor to initialize a PasswordResetToken.
     */
    public PasswordResetToken(User user, String token, LocalDateTime expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
    
    
    public PasswordResetToken() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
     * Checks if the token is expired.
     */
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	

	public PasswordResetToken(Long id, String token, User user, LocalDateTime expiryDate) {
		super();
		this.id = id;
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		return "PasswordResetToken [id=" + id + ", token=" + token + ", user=" + user + ", expiryDate=" + expiryDate
				+ "]";
	}
    
    
}
