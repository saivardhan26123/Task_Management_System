package com.TaskmanagementSystem.Security;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TaskmanagementSystem.Entity.UserAuthenticate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter{
	

	@Autowired
	private JWTTokenUtil jwtToken;

	@Override
	public void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException{
		
		String header=request.getHeader("Authorization");
		
		if (header!=null && header.startsWith("Bearer ")) {
			String token =header.substring(7);
			
			if(jwtToken.validToken(token)) {
				String username = jwtToken.extractUsername(token);

				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						new User(username,"",Collections.emptyList()),null,Collections.emptyList());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid or Expired token");
				return;
			}
		}
	filterChain.doFilter(request,response);
	}
}