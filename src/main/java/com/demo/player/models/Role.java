package com.demo.player.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.demo.emums.RoleType;

@Entity(name = "role")
@Table
public class Role {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private  RoleType roleType;
	
	public Role(Integer id, RoleType roleType) {
		super();
		this.id = id;
		this.roleType = roleType;
	}

	public Role() {
		super();
		this.id = null;
		this.roleType = null;
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	
	
	
}

