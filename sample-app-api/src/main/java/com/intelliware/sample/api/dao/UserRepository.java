package com.intelliware.sample.api.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.intelliware.sample.api.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	List<User> findByUsername(String username);
	
}