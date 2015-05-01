package com.intelliware.sample.api.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.model.Role;
import com.intelliware.sample.api.model.User;
import com.intelliware.sample.vo.IdentityVO;

@RestController
public class IdentityController{
	
	private IdentityVO createIdentityVO(User user) {
		IdentityVO identityVO = new IdentityVO();
		identityVO.setUsername(user.getUsername());
		identityVO.setId(String.valueOf(user.getId()));
		setUserAuthorities(user, identityVO);
		return identityVO;
	}

	private void setUserAuthorities(User user, IdentityVO identityVO) {
		Set	<String> authorities = new HashSet<String>();
		for (Role role : user.getRoles()){
			authorities.add(role.getRoleName());
		}
		identityVO.setAuthorities(authorities);
	}

	@RequestMapping(value="/me")
	public IdentityVO getIdentity() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return createIdentityVO(user);
	}



}
