package com.intelliware.sample.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userDao;

}
