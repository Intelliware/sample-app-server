package com.intelliware.sample.api.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.intelliware.sample.api.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	List<User> findByUsername(String username);
	
	List<User> findByNameLikeIgnoreCase(String name);
	
}