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
import static org.hamcrest.Matchers.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class IdentityControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
        		.addFilters(this.springSecurityFilterChain)
        		.build();
    }
	
    @Test
    public void testGetMe() throws Exception {
    	
        mockMvc.perform(
        			get("/me")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
                  .andExpect(jsonPath("$.id").exists())
                  .andExpect(jsonPath("$.username", is("a")))
        		  .andExpect(jsonPath("$.authorities", hasSize(6)))
        		  .andExpect(jsonPath("$.authorities",
        				  containsInAnyOrder("COMPANY", "COMPANY.EDIT", "COMPANY.CREATE", "USER", "USER.EDIT", "USER.CREATE")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("Company","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("Company")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("COMPANY")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("CompanyEdit","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("CompanyEdit")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("COMPANY.EDIT")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("CompanyCreate","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("CompanyCreate")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("COMPANY.CREATE")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("User","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("User")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("USER")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("UserEdit","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("UserEdit")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("USER.EDIT")));
        
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic("UserCreate","password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is("UserCreate")))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is("USER.CREATE")));

    }
    
    @Test
    public void testGetMe_NotAuthorized() throws Exception {
    	
        mockMvc.perform(
        			get("/me")
        			.with(httpBasic("notAUser","password"))
        		)
                .andExpect(status().is(401));

    }

}
