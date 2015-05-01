package com.intelliware.sample.api;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Iterator;

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
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class UserControllerTest {
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	} 
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private User userA;
    private User userB;
    
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
        userA = userIter.next();
        userB = userIter.next();

    }
    
    @Test
    public void testGetUsers() throws Exception {
    	
        mockMvc.perform(
        			get("/users")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(7)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(7)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userA.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(userA.getName())))
        		  .andExpect(jsonPath("$.elements[0].email", is(userA.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userB.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userB.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userB.getEmail())));
    }
    


}
