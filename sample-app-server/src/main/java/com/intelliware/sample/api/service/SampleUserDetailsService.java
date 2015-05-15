package com.intelliware.sample.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;

@Service
public class SampleUserDetailsService implements UserDetailsService{

	private UserRepository repo;
	
	@Autowired
	public SampleUserDetailsService(UserRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username){
		List<User> results = repo.findByUsername(username);
		return results.get(0);
	}
}
