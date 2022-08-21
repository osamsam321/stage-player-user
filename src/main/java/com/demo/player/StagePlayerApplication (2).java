package com.demo.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.demo.emums.RoleType;
import com.demo.player.models.Role;
import com.demo.player.models.UserSp;
import com.demo.player.services.RoleService;
import com.demo.player.services.UserService;



@SpringBootApplication
public class StagePlayerApplication {

	public static void main(String[] args) {

		SpringApplication.run(StagePlayerApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/greeting-javaconfig").allowedOrigins("localhost:8090/api/login");
//			}
//		};
//	}
	
	
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
		userService.saveUser(new UserSp(null,"testAdmin", "password", "john", "Smith", "js@gmail.com", "9999999999", "9999 west grove", null));
		userService.addRoleToUser("testAdmin", RoleType.ADMIN);		
		userService.saveUser(new UserSp(null,"wills123", "password123", "Will", "Smith", "WS@gmail.com", "9999999999", "9999 west grove", null));	    		
		userService.addRoleToUser("wills123", RoleType.USER_BASIC);
		
	
		
		
		
		
		};
		
	}

}
