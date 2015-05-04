package com.intelliware.sample.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="User not found")
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 8555422913710735475L;

}
