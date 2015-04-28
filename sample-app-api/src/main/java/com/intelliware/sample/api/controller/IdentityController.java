package com.intelliware.sample.api.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.vo.UserVO;

@RestController
public class IdentityController{

	@RequestMapping(value="/me")
	public UserVO user() {
		UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserVO userVO = new UserVO();
		userVO.setUsername(u.getUsername());
		userVO.setId("0");
//		Set<String> authoritySet = AuthorityUtils.authorityListToSet(u.getAuthorities());
		Set<String> authoritySet = new HashSet<String>();
		authoritySet.add("USER");
		authoritySet.add("USER.EDIT");
		authoritySet.add("USER.CREATE");
		authoritySet.add("COMPANY");
		authoritySet.add("COMPANY.EDIT");
		authoritySet.add("COMPANY.CREATE");
		userVO.setAuthorities(authoritySet);
		return userVO;
	}

}
