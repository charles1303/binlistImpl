package com.projects.binlist.dto.requests;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class LoginRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1563875576702880113L;

	@NotBlank
    private String username;

    @NotBlank
    private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
