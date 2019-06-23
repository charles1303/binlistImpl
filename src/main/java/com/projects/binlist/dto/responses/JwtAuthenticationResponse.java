package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8088727426918709804L;
	private String accessToken;
	
	public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
