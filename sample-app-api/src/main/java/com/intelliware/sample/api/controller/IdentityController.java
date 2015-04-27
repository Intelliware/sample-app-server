package com.intelliware.sample.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdentityController {

	@RequestMapping(value="/me")
    public Object user() {
            return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
