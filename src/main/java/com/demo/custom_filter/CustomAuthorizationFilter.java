package com.demo.custom_filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;  

public class CustomAuthorizationFilter extends OncePerRequestFilter {
Logger log =LogManager.getLogger(CustomAuthorizationFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String loginPath = "/login";
		if(request.getServletPath().equals(loginPath))
		{
			log.debug("Enter login page");
			filterChain.doFilter(request, response);
		}
		else
		{
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
			{
				try {
					String token = authorizationHeader.substring("Bearer ".length());
					String secretKey = "weeeeeeeeeefwonewefwfqfwwww,,,,....,ww5151w34534gw{{{{wwwwwwwwwwwfwjoefwefwfiwwwwwwwwwwwwwwwwofewefwefwoffffffffffqweqrlyemzndfs";
					Algorithm algo =  Algorithm.HMAC256(secretKey.getBytes());
					JWTVerifier verifier = JWT.require(algo).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

					for(String role: roles)
					{
						authorities.add(new SimpleGrantedAuthority(role));
					}
					UsernamePasswordAuthenticationToken authenticationToken  = 
							new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					log.error("Error logging in");
					e.printStackTrace();
					response.setHeader("error", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					Map<String,String> error = new HashMap<>();
					error.put("error_message", e.getMessage());
					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				} catch (JWTVerificationException e) {
					// TODO Auto-generated catch block
					log.error("Error loggin in");
					e.printStackTrace();
					response.setHeader("error", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					Map<String,String> error = new HashMap<>();
					error.put("error_message", e.getMessage());
					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), error);
					
				}
				catch(Exception e)
				{
					log.error("Error loggin in");
					e.printStackTrace();
					response.setHeader("error", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					Map<String,String> error = new HashMap<>();
					error.put("error_message", e.getMessage());
					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
			
			}
			
			else
			{
				log.debug("Enter login page");
				filterChain.doFilter(request, response);
			}
		}	

	}

}
