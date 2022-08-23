package com.demo.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.custom_filter.CustomAuthorizationFilter;
import com.demo.player.models.Role;
import com.demo.player.models.UserSp;
import com.demo.player.services.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class HomeController {
	Logger log =LogManager.getLogger(HomeController.class);
	@Autowired
	UserService userService;
	@PostMapping("/signup/new")
	public ResponseEntity<UserSp> post(@RequestBody UserSp user)
	{
		return new ResponseEntity<UserSp>(userService.saveUser(user), HttpStatus.OK);
	}	
	@PostMapping("api/login")
	public ResponseEntity<?> login(@RequestBody UserSp user)
	{
		return ResponseEntity.ok(null);
	}
	@GetMapping("api/getUser/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") Long id)
	{
		log.info("insdie of getUserById method");
		return ResponseEntity.ok(userService.findUserSpByUserId(id));
	}
	@PutMapping("/signup/update")
	public ResponseEntity<UserSp> put(@RequestBody UserSp user)
	{
		return new ResponseEntity<UserSp>(userService.updateUser(user.getId()), HttpStatus.OK);
	}
	@GetMapping("/signup/users")
	public ResponseEntity<List<UserSp>> get()
	{
		return new ResponseEntity<List<UserSp>>(userService.getAllUsers(), HttpStatus.OK);
	}
	@GetMapping("/signup/remove/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id)
	{
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/api/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException
	{
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
		{  
			
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				String secretKey = "weeeeeeeeeefwonewefwfqfwwww,,,,....,ww5151w34534gw{{{{wwwwwwwwwwwfwjoefwefwfiwwwwwwwwwwwwwwwwofewefwefwoffffffffffqweqrlyemzndfs";
				Algorithm algo =  Algorithm.HMAC256(secretKey.getBytes());
				JWTVerifier verifier = JWT.require(algo).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				UserSp user = userService.getUser(username).get();
				String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
				
				String accessToken = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getRoleTypeName).collect(Collectors.toList()))
						.sign(algo);
		
//				response.setHeader("accessToken", accessToken);
//				response.setHeader("refreshToken", refreshToken);
				Map<String,String> tokens = new HashMap<>();
				tokens.put("accessToken", accessToken);
				tokens.put("refreshToken", refresh_token);
				response.setContentType("application/json");
				log.info("new JWT token:" + tokens.toString());
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				
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
			throw new RuntimeException("Refresh tocken is missing");
		}
		
	}
}
