package com.recipe.myrecipe.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Entity
@ToString
@Table(name="user")
public class User {
	@Id
	private String userId;
	private String password;
	private String email;
	private String role;
	private String grantType;

	@CreationTimestamp
	private Timestamp createDate;
}
