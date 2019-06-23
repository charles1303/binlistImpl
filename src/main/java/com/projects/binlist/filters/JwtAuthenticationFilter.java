package com.projects.binlist.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projects.binlist.components.CustomUserDetails;
import com.projects.binlist.components.JwtManager;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    private JwtManager jwtManager;
	
	public void setJwtManager(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}


	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtManager.validateToken(jwt)) {
                String username = jwtManager.getUsernameFromJWT(jwt);
                List<String> roles = jwtManager.getRolesFromJwt(jwt);
                List<GrantedAuthority> authorities = CustomUserDetails.getAuthorities(roles);
                User principal = new User(username, "", authorities);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
            	SecurityContextHolder.getContext().setAuthentication(null);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);

	}
	
	
	private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
