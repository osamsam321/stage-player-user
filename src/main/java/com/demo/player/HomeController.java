package com.demo.player;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.demo.player.models.User;
import com.demo.player.services.UserService;

@RestController
public class HomeController {

	@Autowired
	UserService service;
	@PostMapping("/signup/new")
	public ResponseEntity<User> post(@RequestBody User user)
	{
		return new ResponseEntity<User>(service.saveUser(user), HttpStatus.OK);
	}	
	@PutMapping("/signup/update")
	public ResponseEntity<User> put(@RequestBody User user)
	{
		return new ResponseEntity<User>(service.updateUser(user.getId()), HttpStatus.OK);
	}
	@GetMapping("/signup/users")
	public ResponseEntity<List<User>> get()
	{
		return new ResponseEntity<List<User>>(service.getAllUsers(), HttpStatus.OK);
	}
	@GetMapping("/signup/remove/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id)
	{
		service.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
