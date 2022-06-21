package com.demo.security;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.custom_filter.CustomAuthenticationFilter;
import com.demo.custom_filter.CustomAuthorizationFilter;
import com.demo.emums.RoleType;

import lombok.RequiredArgsConstructor;
import lombok.Data;




@Configuration 
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private  UserDetailsService userDetailService;
	@Autowired
	private  BCryptPasswordEncoder bcEncoder;
	
	public SecurityConfig(UserDetailsService us, BCryptPasswordEncoder bcEncoder)
	{
		this.userDetailService = us;
		this.bcEncoder = bcEncoder;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
	auth.userDetailsService(userDetailService).passwordEncoder(bcEncoder);
	
	}
    
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
    	customAuthenticationFilter.setFilterProcessesUrl("/login");
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/login/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/signup/users/**").hasAnyAuthority(RoleType.ADMIN.toString());
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/signup/new/**").hasAnyAuthority(RoleType.ADMIN.toString());
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(customAuthenticationFilter);
		//http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	


}
