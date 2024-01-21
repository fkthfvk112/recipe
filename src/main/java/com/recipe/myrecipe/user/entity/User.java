package com.recipe.myrecipe.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String userId;

	@Column(nullable = false, unique = true)
	private String nickName;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles = new ArrayList<>();

	private String grantType;

	@CreationTimestamp
	private Timestamp createDate;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}


	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getUsername() {
		return this.userId;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isEnabled() {
		return true;
	}
}
