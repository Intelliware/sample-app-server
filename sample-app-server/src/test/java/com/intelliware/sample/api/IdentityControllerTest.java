package com.intelliware.sample.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppServerApplication.class)
@WebAppConfiguration
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
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
    
    private void performGetMeForUsersWithOneRole(String username, String role) throws Exception{
        mockMvc.perform(
    			get("/me")
    			.with(httpBasic(username,"password"))
    		)
            .andExpect(status().isOk())
              .andExpect(content().contentType(contentType))
              .andExpect(jsonPath("$.id").exists())
              .andExpect(jsonPath("$.username", is(username)))
    		  .andExpect(jsonPath("$.authorities", hasSize(1)))
    		  .andExpect(jsonPath("$.authorities[0]", is(role)));
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
        
        performGetMeForUsersWithOneRole("Company", "COMPANY");
        performGetMeForUsersWithOneRole("CompanyEdit", "COMPANY.EDIT");
        performGetMeForUsersWithOneRole("CompanyCreate", "COMPANY.CREATE");
        performGetMeForUsersWithOneRole("User", "USER");
        performGetMeForUsersWithOneRole("UserEdit", "USER.EDIT");
        performGetMeForUsersWithOneRole("UserCreate", "USER.CREATE");

    }
    
    @Test
    public void testGetMe_NotAuthorized() throws Exception {
    	
        mockMvc.perform(
        			get("/me")
        			.with(httpBasic("notAUser","password"))
        		)
                .andExpect(status().is(401));

    }
    
    @Test
    public void testSignOut() throws Exception {
    	mockMvc.perform(delete("/signOut").with(httpBasic("a","password"))).andExpect(status().is(204));
    }

}
