package com.intelliware.sample.api.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.vo.IdentityVO;

@RestController
public class IdentityController{

	@RequestMapping(value="/me")
	public IdentityVO user() {
		UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		IdentityVO identityVO = new IdentityVO();
		identityVO.setUsername(u.getUsername());
		identityVO.setId("0");
//		Set<String> authoritySet = AuthorityUtils.authorityListToSet(u.getAuthorities());
		Set<String> authoritySet = new HashSet<String>();
		authoritySet.add("USER");
		authoritySet.add("USER.EDIT");
		authoritySet.add("USER.CREATE");
		authoritySet.add("COMPANY");
		authoritySet.add("COMPANY.EDIT");
		authoritySet.add("COMPANY.CREATE");
		identityVO.setAuthorities(authoritySet);
		return identityVO;
	}

}
