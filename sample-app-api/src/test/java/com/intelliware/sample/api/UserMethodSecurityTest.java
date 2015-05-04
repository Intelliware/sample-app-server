package com.intelliware.sample.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class UserMethodSecurityTest {
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	} 
	
	private MockMvc mockMvc;
	private User user;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
        		.addFilters(this.springSecurityFilterChain)
        		.build();
        
        Iterator<User> userIter = userRepository.findAll().iterator();
        user = userIter.next(); //name: A, email: firstUser@email.com

    }

    @Test
    public void testGetUsers_Authorized() throws Exception {
    	
        mockMvc.perform(
        			get("/users")
        			.with(httpBasic("User","password"))
        			)
                .andExpect(status().isOk());
        
        mockMvc.perform(
    			get("/users")
    			.with(httpBasic("UserEdit","password"))
    			)
            .andExpect(status().isOk());
        
        mockMvc.perform(
    			get("/users")
    			.with(httpBasic("UserCreate","password"))
    			)
            .andExpect(status().isOk());
    }
    
    @Test
    public void testGetUsers_NotAuthorized() throws Exception {
    	
        mockMvc.perform(
        			get("/users")
        			.with(httpBasic("Company","password"))
        			)
                .andExpect(status().is(403));
    }
    
    @Test
    public void testGetUser_Authorized() throws Exception {
    	
    	String userId = String.valueOf(user.getId());
    	
        mockMvc.perform(
        		get("/users/" + userId)
        		.with(httpBasic("User","password"))
        		)
                .andExpect(status().isOk());
        
        mockMvc.perform(
        		get("/users/" + userId)
        		.with(httpBasic("UserEdit","password"))
        		)
                .andExpect(status().isOk());
        
        mockMvc.perform(
        		get("/users/" + userId)
        		.with(httpBasic("UserCreate","password"))
        		)
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetUser_NotAuthorized() throws Exception {
    	
    	String userId = String.valueOf(user.getId());
    	
        mockMvc.perform(
        		get("/users/" + userId)
        		.with(httpBasic("Company","password"))
        		)
                .andExpect(status().is(403));
    }
	
}
