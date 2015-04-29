package com.intelliware.sample.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Company not found")
public class CompanyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6174765403031023779L;
}
