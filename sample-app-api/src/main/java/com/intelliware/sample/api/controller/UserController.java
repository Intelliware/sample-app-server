package com.intelliware.sample.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;
import com.intelliware.sample.vo.PageableListVO;
import com.intelliware.sample.vo.UserVO;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userDao;
	
	private Iterable<User> retrieveUsers(String nameToFilterBy, String orderProperty) {
		if (nameToFilterBy != null){
			return userDao.findByNameLikeIgnoreCase("%" + nameToFilterBy + "%");
		}
		if (orderProperty != null){
			return userDao.findAll(new Sort(Sort.Direction.ASC, orderProperty));
		}
		return userDao.findAll();
	}
	
	private UserVO convertToUserVO(User user) {
		UserVO userVO = new UserVO();
		userVO.setId(String.valueOf(user.getId()));
		userVO.setName(user.getName());
		userVO.setEmail(user.getEmail());
		return userVO;
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public PageableListVO<UserVO> getUsers(@RequestParam(required = false, value="name") String nameToFilterBy,
										   @RequestParam(required = false, value="_orderBy") String orderProperty) {
		
		Iterable<User> users = retrieveUsers(nameToFilterBy, orderProperty);
		List<UserVO> userVOList = new ArrayList<UserVO>();
		for (User user : users){
			userVOList.add(convertToUserVO(user));
		}
		return new PageableListVO<UserVO>(userVOList);
	}





}
