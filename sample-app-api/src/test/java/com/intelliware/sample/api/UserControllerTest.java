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
    private User userA; //'ZZ First User Edit', 'firstUser@email.com'
    private User userCompanyAuth; //'Company Auth', 'company.auth@email.com'
    private User userCompanyEditAuth; //'Company Edit Auth', 'company.edit.auth@email.com'
    private User userCompanyCreateAuth; //'Company Create Auth', 'company.create.auth@email.com'
    private User userUserAuth; //'User Auth', 'user.auth@email.com'
    private User userUserEditAuth; //'User Edit Auth', 'user.edit.auth@email.com'
    private User userUserCreateAuth; //'User Create Auth', 'user.create.auth@email.com'
    
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
        userCompanyAuth = userIter.next();
        userCompanyEditAuth = userIter.next();
        userCompanyCreateAuth = userIter.next();
        userUserAuth = userIter.next();
        userUserEditAuth = userIter.next();
        userUserCreateAuth = userIter.next();

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
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userCompanyAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userCompanyAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userCompanyAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(userCompanyCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[3].name", is(userCompanyCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[3].email", is(userCompanyCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(userUserAuth.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(userUserAuth.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(userUserAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[5].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[5].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[5].email", is(userUserEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[6].id", is(String.valueOf(userUserCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[6].name", is(userUserCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[6].email", is(userUserCreateAuth.getEmail())));
    }
    
    @Test
    public void testGetUsers_WithFilterResults() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=creaTE")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userUserCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userUserCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userUserCreateAuth.getEmail())));
    }
    
    @Test
    public void testGetUsers_WithFilterNoResults() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=PAULA ABDUL")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(0)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(0)));
    }
    
    @Test
    public void testGetUsers_OrderByName() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(7)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(7)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userCompanyCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userCompanyCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userCompanyCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(userUserAuth.getId()))))
				  .andExpect(jsonPath("$.elements[3].name", is(userUserAuth.getName())))
				  .andExpect(jsonPath("$.elements[3].email", is(userUserAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(userUserCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(userUserCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(userUserCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[5].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[5].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[5].email", is(userUserEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[6].id", is(String.valueOf(userA.getId()))))
	      		  .andExpect(jsonPath("$.elements[6].name", is(userA.getName())))
	      		  .andExpect(jsonPath("$.elements[6].email", is(userA.getEmail())));
    }
    
    @Test
    public void testGetUsers_OrderByEmail() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_orderBy=email")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(7)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(7)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userCompanyCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userCompanyCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userCompanyCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(userA.getId()))))
	      		  .andExpect(jsonPath("$.elements[3].name", is(userA.getName())))
	      		  .andExpect(jsonPath("$.elements[3].email", is(userA.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(userUserAuth.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(userUserAuth.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(userUserAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[5].id", is(String.valueOf(userUserCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[5].name", is(userUserCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[5].email", is(userUserCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[6].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[6].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[6].email", is(userUserEditAuth.getEmail())));

    }
    
    @Test
    public void testGetUsers_FilterAndOrderByName() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=edit&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(3)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userUserEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userA.getId()))))
		  		  .andExpect(jsonPath("$.elements[2].name", is(userA.getName())))
		  		  .andExpect(jsonPath("$.elements[2].email", is(userA.getEmail())));
    }
    
    @Test
    public void testGetUsers_FilterAndOrderByEmail() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=edit&_orderBy=email")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(3)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userA.getId()))))
		  		  .andExpect(jsonPath("$.elements[1].name", is(userA.getName())))
		  		  .andExpect(jsonPath("$.elements[1].email", is(userA.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(userUserEditAuth.getEmail())));
    }
    
    @Test
    public void testGetUsers_Pagination() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_pageNumber=2&_pageSize=3")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(3)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userUserAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userUserAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userUserAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(userUserEditAuth.getEmail())));
    }
    
    public void testGetUsers_PaginationLastPage() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_pageNumber=3&_pageSize=3")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(1)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(1)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userUserEditAuth.getEmail())));
    }
    
    @Test
    public void testGetUsers_PaginationOutOfRange() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_pageNumber=2&_pageSize=7")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(0)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(0)));
    }
    
    @Test
    public void testGetUsers_PaginationAndOrderBy() throws Exception {
    	
        mockMvc.perform(
        			get("/users?_pageNumber=3&_pageSize=2&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(2)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userUserCreateAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userUserCreateAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userUserCreateAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userUserEditAuth.getEmail())));
    }
    
    @Test
    public void testGetUsers_PaginationAndFilter() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=edit&_pageNumber=1&_pageSize=1")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(1)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(1)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userA.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userA.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userA.getEmail())));
    }
    
    @Test
    public void testGetUsers_PaginationAndFilterAndOrderBy() throws Exception {
    	
        mockMvc.perform(
        			get("/users?name=edit&_pageNumber=1&_pageSize=2&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(userCompanyEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(userCompanyEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(userCompanyEditAuth.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(userUserEditAuth.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(userUserEditAuth.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(userUserEditAuth.getEmail())));
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
    
    


    


}
