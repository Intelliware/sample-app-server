package com.intelliware.sample.api.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdentityController {

	@RequestMapping(value="/authentication", method=RequestMethod.POST)//, headers={"content-type=application/json"})
	public Principal user(Principal user) {
		return user;
	}

}
