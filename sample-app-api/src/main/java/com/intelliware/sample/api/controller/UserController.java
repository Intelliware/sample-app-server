package com.intelliware.sample.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;
import com.intelliware.sample.vo.PageableListVO;
import com.intelliware.sample.vo.UserVO;

@RestController
public class UserController implements IConstants {
	
	@Autowired
	private UserRepository userDao;
	
	private List<UserVO> convertToUserVOList(Iterable<User> users) {
		List<UserVO> userVOList = new ArrayList<UserVO>();
		for (User user : users){
			userVOList.add(convertToUserVO(user));
		}
		return userVOList;
	}

	private Sort getSort(String orderProperty) {
		return orderProperty == null ? null : new Sort(Sort.Direction.ASC, orderProperty);
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
	
	public String getFilterString(String nameToFilterBy){
		return new StringBuilder().append("%").append(nameToFilterBy).append("%").toString();
	}
	
	public PageableListVO<UserVO> getUsersNotPaginated(String nameToFilterBy, String orderProperty){	
		Sort sort = getSort(orderProperty);
		
		Iterable<User> users = nameToFilterBy == null ? 
				userDao.findAll(sort) : //sort can be null;
				userDao.findByNameLikeIgnoreCase(getFilterString(nameToFilterBy), sort);
	
		List<UserVO> userVOList = convertToUserVOList(users);
		return new PageableListVO<UserVO>(userVOList);
	}

	
	public PageableListVO<UserVO> getUsersPaginated(String nameToFilterBy, String orderProperty, Integer page, Integer pageSize){
		Sort sort = getSort(orderProperty);
		PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort); //subtract 1 because pageRequest is 0 based
		
		Page<User> users = nameToFilterBy == null ? 
				userDao.findAll(pageRequest) :
				userDao.findByNameLikeIgnoreCase(getFilterString(nameToFilterBy), pageRequest);
				
		List<UserVO> userVOList = convertToUserVOList(users);
		return new PageableListVO<UserVO>(userVOList, users.getTotalElements());
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT', 'USER')")
	@RequestMapping(value="/users", method=RequestMethod.GET, produces=IConstants.jsonUTF8)
	public PageableListVO<UserVO> getUsers(@RequestParam(required = false, value="name") String nameToFilterBy,
										   @RequestParam(required = false, value="_orderBy") String orderProperty,
										   @RequestParam(required = false, value="_pageNumber") Integer page,
										   @RequestParam(required = false, value="_pageSize") Integer pageSize) {
		if (page == null || pageSize == null){
			return getUsersNotPaginated(nameToFilterBy, orderProperty);
		} else {
			return getUsersPaginated(nameToFilterBy, orderProperty, page, pageSize);
		}
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT', 'USER')")
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET, produces=IConstants.jsonUTF8)
	public UserVO getUser(@PathVariable String id) throws UserNotFoundException {		
		User user = findUser(id);
		return convertToUserVO(user);
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER.CREATE')")
	@RequestMapping(value="/users", method=RequestMethod.POST, consumes=IConstants.jsonUTF8)
	@ResponseStatus(HttpStatus.CREATED)
	public UserVO addUser(@RequestBody UserVO inputUser) {
		User user = createUser(inputUser);
		userDao.save(user);
		return convertToUserVO(user);
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER.CREATE', 'USER.EDIT')")
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT, consumes=IConstants.jsonUTF8)
	public UserVO updateUser(@PathVariable String id, @RequestBody UserVO inputUser) throws UserNotFoundException{
		User user = findUser(id);
		setUserAttributes(inputUser, user);
		userDao.save(user);
		return convertToUserVO(user);
	}
	
	@Transactional
	@PreAuthorize("hasRole('USER.CREATE')")
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable String id) throws UserNotFoundException {
		User user = findUser(id);
		userDao.delete(user);
	}
}
