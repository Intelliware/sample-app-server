package com.intelliware.sample.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;
import com.intelliware.sample.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class UserMethodSecurityTest {
	
	private MockMvc mockMvc;
	private List<User> userList;
	private String userId;
	private UserVO requestBody = TestUtils.createMyUserVO();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    
    private void performGetUsers(List<String> usernames, ResultMatcher expectedStatus) throws Exception {
    	for (String username : usernames){
	        mockMvc.perform(
	    			get("/users")
	    			.with(httpBasic(username,"password"))
	    			)
	            .andExpect(expectedStatus);
    	}
	}
    
    private void performGetUser(List<String> usernames, ResultMatcher expectedStatus) throws Exception {
    	for (String username : usernames){
	        mockMvc.perform(
	        		get("/users/" + userId)
	        		.with(httpBasic(username, "password"))
	        		)
	                .andExpect(expectedStatus);
    	}
	}
    
    private void performAddUser(List<String> usernames, ResultMatcher expectedStatus) throws Exception{
		for (String username : usernames){
	    	mockMvc.perform(
	    			post("/users")
	    			.with(httpBasic(username,"password"))
	    			.content(TestUtils.asJsonString(requestBody))
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			)
	    	  .andExpect(expectedStatus);
		}
	}
    
    private void performUpdateUser(List<String> usernames, ResultMatcher expectedStatus) throws Exception {
    	for (String username : usernames){
        	mockMvc.perform(
        			put("/users/" + userId)
        			.with(httpBasic(username,"password"))
        			.content(TestUtils.asJsonString(requestBody))
        			.contentType(MediaType.APPLICATION_JSON)
        			.accept(MediaType.APPLICATION_JSON)
        			)
        	  .andExpect(expectedStatus);
    	}
	}
    
    private void performDeleteUser(List<String> usernames, ResultMatcher expectedStatus) throws Exception {
    	String userToDeleteId = String.valueOf(userList.get(2).getId());
    	for (String username : usernames){
        	mockMvc.perform(
        			delete("/users/" + userToDeleteId)
        			.with(httpBasic(username,"password"))
        			.content(TestUtils.asJsonString(requestBody))
        			.contentType(MediaType.APPLICATION_JSON)
        			.accept(MediaType.APPLICATION_JSON)
        			)
        	  .andExpect(expectedStatus);
    	}
	}

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
        		.addFilters(this.springSecurityFilterChain)
        		.build();
        
        Iterator<User> userIter = userRepository.findAll().iterator();
        userList = Lists.newArrayList(userIter);
        userId = String.valueOf(userList.get(1).getId());
    }

    @Test
    public void testGetUsers_Authorized() throws Exception {
    	performGetUsers(Arrays.asList("User", "UserEdit", "UserCreate"), status().isOk());
    }

	@Test
    public void testGetUsers_NotAuthorized() throws Exception {
		performGetUsers(Arrays.asList("Company"), status().is(403));
    }
    
    @Test
    public void testGetUser_Authorized() throws Exception {
    	performGetUser(Arrays.asList("User", "UserEdit", "UserCreate"), status().isOk());
    }

	@Test
    public void testGetUser_NotAuthorized() throws Exception {
		performGetUser(Arrays.asList("Company"), status().is(403));
    }
    
    @Test
    public void testAddUser_Authorized() throws Exception {
    	performAddUser(Arrays.asList("UserCreate"), status().isOk());
    }
    
	@Test
    public void testAddUser_NotAuthorized() throws Exception {
		performAddUser(Arrays.asList("User", "UserEdit", "Company"), status().is(403));
    }
    
    @Test
    public void testUpdateUser_Authorized() throws Exception {
    	performUpdateUser(Arrays.asList("UserCreate", "UserEdit"), status().isOk());
    }
    
	@Test
    public void testUpdateUser_NotAuthorized() throws Exception {
		performUpdateUser(Arrays.asList("User", "Company"), status().is(403));
    }
    
    @Test
    public void testDeleteUser_Authorized() throws Exception {
    	performDeleteUser(Arrays.asList("UserCreate"), status().is(204));
    }

	@Test
    public void testDeleteUser_NotAuthorized() throws Exception {
		performDeleteUser(Arrays.asList("UserEdit", "User", "Company"), status().is(403));
    }
}
