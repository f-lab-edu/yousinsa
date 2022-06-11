package com.flab.yousinsa.user.domain.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flab.yousinsa.store.domain.Store;
import com.flab.yousinsa.user.domain.enums.UserRole;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@NotNull
	@Column(name = "user_name")
	private String userName;
	@NotNull
	@Email
	@Column(name = "user_email")
	private String userEmail;
	@NotNull
	@Column(name = "user_password")
	private String userPassword;
	@Enumerated(value = EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@JsonIgnore
	@OneToOne(mappedBy = "storeOwner", cascade = CascadeType.ALL)
	private Store store;

	public UserEntity() {
	}

	public UserEntity(String userName, String userEmail, String userPassword, UserRole userRole) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
