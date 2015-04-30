package com.intelliware.sample.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
    
    private List<User> userList = new ArrayList<>();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        
        userRepository.deleteAll();
        
        User user = new User();
        user.setUsername("c_babbs");
        user.setPassword("password");
        user.setName("Charles Babbage");
        user.setEmail("computer_dad_1791@gmail.com");
        userList.add(userRepository.save(user));
        
        user = new User();
        user.setUsername("countess_lovelace");
        user.setPassword("password");
        user.setName("Ada Lovelace");
        user.setEmail("countess_1815@gmail.com");
        userList.add(userRepository.save(user));
        
    }
    
    @Test
    public void testGetUsers() throws Exception {

    	User charles = this.userList.get(0);
    	User ada = this.userList.get(1);
    	
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(charles.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(charles.getName())))
        		  .andExpect(jsonPath("$.elements[0].email", is(charles.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(ada.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(ada.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(ada.getEmail())));
    }

}
