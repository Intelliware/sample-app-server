package com.intelliware.sample.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	private Iterable<User> retrieveUsers(String nameToFilterBy, String orderProperty, Integer page, Integer pageSize) {
		Sort sort = orderProperty == null ? null : new Sort(Sort.Direction.ASC, orderProperty);
		PageRequest pageRequest = page == null || pageSize == null ? null :
								  	new PageRequest(page - 1, pageSize, sort); //subtract 1 because pageRequest is 0 based		
		if (nameToFilterBy != null){
			String filterString = "%" + nameToFilterBy + "%";
			return pageRequest != null ? userDao.findByNameLikeIgnoreCase(filterString, pageRequest) : 
										 userDao.findByNameLikeIgnoreCase(filterString, sort);
		}
		if (pageRequest != null){
			return userDao.findAll(pageRequest);
		}	
		return userDao.findAll(sort); //sort can be null
	}
	
	private User findUser(String id) throws UserNotFoundException {
		User user = userDao.findOne(Long.valueOf(id));
		if (user == null){
			throw new UserNotFoundException();
		}
		return user;
	}
	

	private User createUser(UserVO inputUser) {
		User newUser = new User();
		setUserAttributes(inputUser, newUser);
		return newUser;
	}

	public void setUserAttributes(UserVO userVO, User user) {
		user.setName(userVO.getName());
		user.setEmail(userVO.getEmail());
	}
	
	private UserVO convertToUserVO(User user) {
		UserVO userVO = new UserVO();
		userVO.setId(String.valueOf(user.getId()));
		userVO.setName(user.getName());
		userVO.setEmail(user.getEmail());
		return userVO;
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT', 'USER')")
	@RequestMapping(value="/users", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public PageableListVO<UserVO> getUsers(@RequestParam(required = false, value="name") String nameToFilterBy,
										   @RequestParam(required = false, value="_orderBy") String orderProperty,
										   @RequestParam(required = false, value="_pageNumber") Integer page,
										   @RequestParam(required = false, value="_pageSize") Integer pageSize) {		
		Iterable<User> users = retrieveUsers(nameToFilterBy, orderProperty, page, pageSize);
		List<UserVO> userVOList = new ArrayList<UserVO>();
		for (User user : users){
			userVOList.add(convertToUserVO(user));
		}
		return new PageableListVO<UserVO>(userVOList);
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT', 'USER')")
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public UserVO getUser(@PathVariable String id) throws UserNotFoundException {		
		User user = findUser(id);
		return convertToUserVO(user);
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER.CREATE')")
	@RequestMapping(value="/users", method=RequestMethod.POST, consumes="application/json;charset=UTF-8")
	public UserVO addUser(@RequestBody UserVO inputUser) {
		User user = createUser(inputUser);
		userDao.save(user);
		return convertToUserVO(user);
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT')")
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT, consumes="application/json;charset=UTF-8")
	public UserVO updateUser(@PathVariable String id, @RequestBody UserVO inputUser) throws UserNotFoundException{
		User user = findUser(id);
		setUserAttributes(inputUser, user);
		userDao.save(user);
		return convertToUserVO(user);
	}




}
