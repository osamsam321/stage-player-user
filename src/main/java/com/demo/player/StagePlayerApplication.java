package com.demo.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.demo.emums.RoleType;
import com.demo.player.models.Role;
import com.demo.player.models.User;
import com.demo.player.services.RoleService;
import com.demo.player.services.UserService;



@SpringBootApplication
public class StagePlayerApplication {

	public static void main(String[] args) {

		SpringApplication.run(StagePlayerApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService)
	{
		return args ->
		{
		for (RoleType roleType: RoleType.values())
		{
			roleService.saveRole(new Role(null, roleType ));
		}
//		private UUID id;
//		private String userName;
//		private String firstName;
//		private String lastName;
//		private String email;
//		private String phoneNum;
//		private String address;
//		@ManyToMany(fetch = FetchType.EAGER)
//		private ArrayList<Role> roles;
		userService.saveUser(new User(null,"testAdmin", "password", "john", "Smith", "js@gmail.com", "9999999999", "9999 west grove", null));
		userService.addRoleToUser("testAdmin", RoleType.ADMIN);		
		userService.saveUser(new User(null,"wills123", "password123", "Will", "Smith", "WS@gmail.com", "9999999999", "9999 west grove", null));	    		
		userService.addRoleToUser("wills123", RoleType.USER_BASIC);
		
	
		
		
		
		
		};
		
	}

}
