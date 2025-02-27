package com.spring.vaidya.entity;

import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;  // For OpenAPI (Springdoc)
import jakarta.persistence.*;

@Entity
@Table(name="confirmTokenDoctor")
@Schema(description = "Entity representing a confirmation token for doctor registration")
public class ConfirmTokenDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    @Schema(description = "Unique ID for the confirmation token", example = "1")
    private Long tokenId;

    @Column(name="confirm_token_Doctor", nullable = false, unique = true)
    @Schema(description = "Unique confirmation token for the doctor", example = "b3d1c3b1-53a5-4d4a-8f2d-3498c5f3b6d2")
    private String confirmTokenDoctor;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Timestamp when the confirmation token was created", example = "2024-02-24T12:34:56")
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "doctor_id")
    @Schema(description = "Doctor entity associated with this confirmation token")
    private User doctor;

    public ConfirmTokenDoctor() {}

    public ConfirmTokenDoctor(User doctor) {
        this.doctor = doctor;
        this.createdDate = new Date();
        this.confirmTokenDoctor = UUID.randomUUID().toString();
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmTokenDoctor;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmTokenDoctor = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getDoctorEntity() {
        return doctor;
    }

    public void setDoctorEntity(User doctor) {
        this.doctor = doctor;
    }
}
