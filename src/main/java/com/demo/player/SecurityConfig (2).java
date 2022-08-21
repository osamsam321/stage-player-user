package com.demo.player;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.demo.custom_filter.CustomAuthenticationFilter;
import com.demo.custom_filter.CustomAuthorizationFilter;
import com.demo.emums.RoleType;

import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




@EnableWebSecurity 
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
	@Autowired
	private  UserDetailsService userDetailService;
	@Autowired
	private  BCryptPasswordEncoder bcEncoder;
	Logger log = LogManager.getLogger(SecurityConfig.class);
	
	public SecurityConfig(UserDetailsService us, BCryptPasswordEncoder bcEncoder)
	{
		log.info("inside of default securityconfig constructor");
		this.userDetailService = us;
		this.bcEncoder = bcEncoder;
	}
    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	log.info("inside of http security configure method");	
    	http.cors().and();
         
    	CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
    	customAuthenticationFilter.setFilterProcessesUrl("/api/login/**");
		http.csrf().disable();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/api/token/refresh/**").permitAll();
		http.authorizeRequests().antMatchers("/api/login/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/signup/users/**").hasAnyAuthority(RoleType.ADMIN.toString());
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/signup/new/**").hasAnyAuthority(RoleType.ADMIN.toString());
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/getUser/**").hasAnyAuthority( RoleType.USER_BASIC.toString());
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(customAuthenticationFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
	auth.userDetailsService(userDetailService).passwordEncoder(bcEncoder);
	
	}


	 @Override @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	 
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type","Accept", "Authorization", "Origin, Accept", "X-Requested-With","Access-Control-Request-Method", "Access-Control-Request-Headers"));
	        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	 }
	 
//	 @Bean
//	    public CorsFilter corsFilter() {
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration config = new CorsConfiguration();
//	        config.setAllowCredentials(true);
//	        config.addAllowedOrigin("*");
//	        config.addAllowedHeader("*");
//	        config.addAllowedMethod("OPTIONS");
//	        config.addAllowedMethod("GET");
//	        config.addAllowedMethod("POST");
//	        config.addAllowedMethod("PUT");
//	        config.addAllowedMethod("DELETE");
//	        source.registerCorsConfiguration("/**", config);
//	        return new CorsFilter(source);
//	    }
//	

//	 @Bean
//		public CorsFilter corsFilter() {
//			CorsConfiguration corsConfiguration = new CorsConfiguration();
//			corsConfiguration.setAllowCredentials(true);
//			corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//			corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//					"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
//					"Access-Control-Request-Method", "Access-Control-Request-Headers"));
//			corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
//					"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//			corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//			urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//			return new CorsFilter(urlBasedCorsConfigurationSource);
//		}
//

	


}