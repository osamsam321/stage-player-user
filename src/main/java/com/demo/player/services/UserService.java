package com.demo.player.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.emums.RoleType;
import com.demo.player.models.Role;
import com.demo.player.models.User;
import com.demo.player.repo.RoleRepo;
import com.demo.player.repo.UserRepo;
@Service
@Transactional 
public class UserService implements UserDetailsService {
@Autowired
UserRepo userRepo;
@Autowired
RoleRepo roleRepo;
@Autowired
private  PasswordEncoder passwordEncoder;
Logger log = LogManager.getLogger(UserService.class);
	public User saveUser(User user)
	{
		if(userExists(user.getUsername()))
		{
			
		}
		else if(emailExists(user.getEmail()))
		{
			
		}
		else
		{
			log.info("save user with name {} to database", user.getUserName());
			user.setPassWord(passwordEncoder.encode(user.getPassWord()));
			userRepo.save(user);
		}
		
		return null ;
	}
	public boolean userExists(String username)
	{
		return userRepo.findByUserName(username).isPresent();
	}
	public boolean emailExists(String email)
	{
		return userRepo.findByEmail(email).isPresent();
	}
	public User getUser(UUID id)
	{
		return userRepo.getById(id);
		
	}
	public List<User> getAllUsers()
	{
		return userRepo.findAll();
	}
	public List<User> saveUsers()
	{
		return userRepo.findAll();
	}
	public void addRoleToUser(String username, RoleType roleType )
	{
		User user = userRepo.findByUserName(username).get();
		Role role = roleRepo.findByRoleName(roleType.name()).get();
		user.getRoles().add(role);
	}
	public User updateUser(UUID uuid)
	{
		return userRepo.findUserById(uuid).get();
				/*.orElseThrow(() -> new UserNotFoundException("User ID: " + " was not found")); */
	}
	public void deleteUser(UUID id)
	{
		 userRepo.deleteById(id);
	}
	
	public Optional<User> findByUsername(String username)
	{
		return userRepo.findByUserName(username);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userFromDB = findByUsername(username);
		
		if(userFromDB.isEmpty())
		{
			log.error("user was not found and is null");
			return null;
//			throw new UserNotFoundException("User was not found");
		}
		else
		{
			log.info("user was found in the database");
			User user = userFromDB.get();
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			user.getRoles().forEach(role -> authorities.add(
					new SimpleGrantedAuthority(role.getRoleType().name())));
			
			return new org.springframework.security.core.userdetails.User(user.getUserName(), 
					user.getPassWord(), authorities);
		}
		
	}
}
