package com.demo.player.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.player.models.Role;
import com.demo.player.repo.RoleRepo;
@Service
public class RoleService {
	@Autowired
	RoleRepo repo;
	public Role addRole(Role role)
	{
		return repo.save(role);
	}
	public Role getRole(UUID id)
	{
		return repo.getById(id);		
	}
	public ArrayList<Role> getAllRoles()
	{
		return (ArrayList<Role>) repo.findAll();
	}
	public void saveRoles(ArrayList <Role> roles)
	{
		 repo.saveAll(roles);
	}
	public void saveRole(Role role)
	{
		 repo.save(role);
	}
	public Role updateRole(UUID uuid)
	{
		return null;
//		return repo.findRoleById(uuid)
//				.orElseThrow(() -> new RoleNotFoundException("Role ID: " + " was not found"));
	}
	public void deleteRole(UUID id)
	{
		 repo.deleteById(id);
	}
}	
