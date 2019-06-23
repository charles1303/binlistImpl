package com.projects.binlist.components;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomRestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomRestAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());
		httpResponse.setContentType("application/json;charset=UTF-8");
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		try {
			httpResponse.getWriter().write(new JSONObject()
			        .put("timestamp", LocalDateTime.now())
			        .put("description", "Unauthorized user")
			        .put("code", HttpServletResponse.SC_UNAUTHORIZED)
			        .toString());
		} catch (JSONException e) {
			logger.error("Error generating Unauthorized response message - {}", e.getMessage());
		}

	}

}
