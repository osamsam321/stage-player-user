package com.demo.player.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserSpDetail implements UserDetails{
	String username;
	String password;
	Long userid;
	boolean isAccountNonExpired;
	boolean isAccountNonLocked;
	boolean isCredentialsNonExpired;
	boolean isEnabled;
	Collection <SimpleGrantedAuthority> ga;
public UserSpDetail()
{
	
}
public UserSpDetail(String username, String password, Long userid, Collection <SimpleGrantedAuthority> ga )
{
	this.username = username;
	this.password = password;
	this.userid = userid;
	this.ga = ga;
	this.isAccountNonExpired = true;
	this.isAccountNonLocked = true;
	this.isCredentialsNonExpired = true;
	this.isEnabled = true;
	
}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {	
		 return ga;	   
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	

	
}
