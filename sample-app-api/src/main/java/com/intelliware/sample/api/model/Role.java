package com.intelliware.sample.api.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = -7223267594660219489L;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"),
    			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String roleName;

	public Role(){}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getAuthority() {
		return getRoleName();
	}
}
