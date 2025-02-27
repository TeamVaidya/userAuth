package com.spring.vaidya.entity;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID for the user", example = "1")
    private Long userId;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the user", example = "Dr. John Doe")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User's email address", example = "johndoe@example.com")
    private String userEmail;

    @Schema(description = "User's specialization", example = "Cardiology")
    private String specialization;

    @Schema(description = "User's qualification", example = "MBBS, MD")
    private String qualification;

    @Schema(description = "Years of experience", example = "10")
    private int experience;

    @Schema(description = "User's address", example = "123 Street, City, Country")
    private String address;

    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    @Pattern(regexp = "\\d{10}", message = "Phone number must contain only digits")
    @Schema(description = "User's phone number", example = "9876543210")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", example = "securePassword123")
    private String password;

    @Schema(description = "Diseases user is treating", example = "Diabetes, Hypertension")
    private String diseases;

    @Schema(description = "Clinic name", example = "City Health Clinic")
    private String clinicName;

    @Schema(description = "Clinic opening time", example = "08:00:00")
    private LocalTime openTime;

    @Schema(description = "Clinic closing time", example = "17:00:00")
    private LocalTime closeTime;

    @Schema(description = "User activation status", example = "true")
    private boolean isEnabled = false;

    @Schema(description = "Role ID", example = "1")
    private Integer roleId = 1;

    @NotBlank(message = "Aadhar number is required")
    @Size(min = 12, max = 12, message = "Aadhar number must be 12 digits")
    @Pattern(regexp = "\\d{12}", message = "Aadhar number must contain only digits")
    @Schema(description = "User's Aadhar number", example = "123456789012")
    private String aadharNo;
    
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDiseases() {
		return diseases;
	}
	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public LocalTime getOpenTime() {
		return openTime;
	}
	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}
	public LocalTime getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getAadharNo() {
		return aadharNo;
	}
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}
	public User(Long userId, String fullName, String userEmail, String specialization, String qualification,
			int experience, String address, String gender, String phoneNumber, String password, String diseases,
			String clinicName, LocalTime openTime, LocalTime closeTime, boolean isEnabled, Integer roleId,
			String aadharNo) {
		super();
		this.userId = userId;
		this.fullName = fullName;
		this.userEmail = userEmail;
		this.specialization = specialization;
		this.qualification = qualification;
		this.experience = experience;
		this.address = address;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.diseases = diseases;
		this.clinicName = clinicName;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.isEnabled = isEnabled;
		this.roleId = roleId;
		this.aadharNo = aadharNo;
	}
	public User() {
		super();
	}
	public User(String userEmail2, String password2, Object object) {
		// TODO Auto-generated constructor stub
	}
    
  
}

