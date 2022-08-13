package com.demo.custom_filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo.player.models.UserSpDetail;
import com.demo.player.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	Logger log = LogManager.getLogger(CustomAuthenticationFilter.class);
	private final AuthenticationManager authenticationManager;
	
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		log.info("username is {}", username);
		log.info("password is {}", password);
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
		log.info("this user has been authenticated and now has a authtoken");
		return authenticationManager.authenticate(authToken);
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		log.info("login was very succesful");
		UserSpDetail user = (UserSpDetail) authentication.getPrincipal();
		log.info("user authorities: " + user.getAuthorities().toString());
		String secretKey = "weeeeeeeeeefwonewefwfqfwwww,,,,....,ww5151w34534gw{{{{wwwwwwwwwwwfwjoefwefwfiwwwwwwwwwwwwwwwwofewefwefwoffffffffffqweqrlyemzndfs";
		Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
		
		String accessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		String refreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString())
				.sign(algorithm);
//		response.setHeader("accessToken", accessToken);
//		response.setHeader("refreshToken", refreshToken);
		Map<String,String> tokens = new HashMap<>();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		tokens.put("userid",  user.getUserid() + "");
		
		response.setContentType("application/json");
		log.info("new JWT token:" + tokens.toString());
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		
	}
}
