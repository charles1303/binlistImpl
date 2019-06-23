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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomRestAccessDenied implements AccessDeniedHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomRestAccessDenied.class);

	@Override
	public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.error("Responding with Access Denied error. Message - {}", accessDeniedException.getMessage());
		httpResponse.setContentType("application/json;charset=UTF-8");
		httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		try {
			httpResponse.getWriter().write(new JSONObject()
			        .put("timestamp", LocalDateTime.now())
			        .put("description", "Access denied")
			        .put("code", HttpServletResponse.SC_FORBIDDEN)
			        .toString());
		} catch (JSONException e) {
			logger.error("Error generating forbidden response message - {}", e.getMessage());
		}
		

	}

}
