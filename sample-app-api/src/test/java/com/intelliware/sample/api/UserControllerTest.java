package com.intelliware.sample.api;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
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
import org.springframework.web.context.WebApplicationContext;

import com.intelliware.sample.api.dao.UserRepository;
import com.intelliware.sample.api.model.User;
import com.intelliware.sample.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class UserControllerTest {
	
	private MediaType contentType = TestUtils.getContentType();
    private MockMvc mockMvc;
    List<User> userList = new ArrayList<User>();
    private UserVO requestBody = TestUtils.createMyUserVO();
    
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
        User user = userIter.next(); //name: A, email: firstUser@email.com
        userList.add(userRepository.save(user));
        
        while (userIter.hasNext()){
        	userRepository.delete(userIter.next());
        }
        
        user = new User();
        user.setName("Dorothy Gale");
        user.setEmail("i_love_toto@email.com");
        userList.add(userRepository.save(user));
        
        user = new User();
        user.setName("Pippin 1");
        user.setEmail("peregrin.took@email.com");
        userList.add(userRepository.save(user));
        
        user = new User();
        user.setName("Pippin 2");
        user.setEmail("other_pip@email.com");
        userList.add(userRepository.save(user));
        
        
        user = new User();
        user.setName("Mary Poppins");
        user.setEmail("super.nanny@email.com");
        userList.add(userRepository.save(user));
    }
    
    @Test
    public void testGetUsers() throws Exception {
    	
    	User user1 = userList.get(0);
    	User user2 = userList.get(1);
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(5)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user1.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(user1.getName())))
        		  .andExpect(jsonPath("$.elements[0].email", is(user1.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user2.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user2.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[3].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[3].email", is(user4.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(user5.getEmail())));
    }
    
    @Test
    public void testGetUsers_WithFilterResults() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?name=piN")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(3)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user4.getEmail())))
  				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(user5.getEmail())));
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
    	
    	User user1 = userList.get(0);
    	User user2 = userList.get(1);
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(5)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user1.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user1.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user1.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user2.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user2.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(user5.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[3].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[3].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(user4.getEmail())));
    }
    
    @Test
    public void testGetUsers_OrderByEmail() throws Exception {
    	
    	User user1 = userList.get(0);
    	User user2 = userList.get(1);
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
    	
        mockMvc.perform(
        			get("/users?_orderBy=email")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(5)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user1.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user1.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user1.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user2.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user2.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(user4.getEmail())))
				  .andExpect(jsonPath("$.elements[3].id", is(String.valueOf(user3.getId()))))
	      		  .andExpect(jsonPath("$.elements[3].name", is(user3.getName())))
	      		  .andExpect(jsonPath("$.elements[3].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[4].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[4].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[4].email", is(user5.getEmail())));

    }
    
    @Test
    public void testGetUsers_FilterAndOrderByName() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?name=Pi&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(3)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user5.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user4.getId()))))
		  		  .andExpect(jsonPath("$.elements[2].name", is(user4.getName())))
		  		  .andExpect(jsonPath("$.elements[2].email", is(user4.getEmail())));
    }
    
    @Test
    public void testGetUsers_FilterAndOrderByEmail() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?name=Pi&_orderBy=email")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(3)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user4.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user3.getId()))))
		  		  .andExpect(jsonPath("$.elements[1].name", is(user3.getName())))
		  		  .andExpect(jsonPath("$.elements[1].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[2].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[2].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[2].email", is(user5.getEmail())));
    }
    
    @Test
    public void testGetUsers_Pagination() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user4 = userList.get(3);
    	
        mockMvc.perform(
        			get("/users?_pageNumber=2&_pageSize=2")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(2)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user3.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user4.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user4.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user4.getEmail())));
    }
    
    public void testGetUsers_PaginationLastPage() throws Exception {
    	
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?_pageNumber=2&_pageSize=4")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(1)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user5.getEmail())));
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
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)));
    }
    
    @Test
    public void testGetUsers_PaginationAndOrderBy() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?_pageNumber=2&_pageSize=2&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
	      		  .andExpect(jsonPath("$.elements", hasSize(2)))
	      		  .andExpect(jsonPath("$._metadata.totalElements", is(5)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user5.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user3.getEmail())));
    }
    
    @Test
    public void testGetUsers_PaginationAndFilter() throws Exception {
    	
    	User user3 = userList.get(2);
    	
        mockMvc.perform(
        			get("/users?name=pI&_pageNumber=1&_pageSize=1")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(1)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user3.getEmail())));
    }
    
    @Test
    public void testGetUsers_PaginationAndFilterAndOrderBy() throws Exception {
    	
    	User user3 = userList.get(2);
    	User user5 = userList.get(4);
    	
        mockMvc.perform(
        			get("/users?name=PI&_pageNumber=1&_pageSize=2&_orderBy=name")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(3)))
				  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(user5.getId()))))
				  .andExpect(jsonPath("$.elements[0].name", is(user5.getName())))
				  .andExpect(jsonPath("$.elements[0].email", is(user5.getEmail())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(user3.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(user3.getName())))
				  .andExpect(jsonPath("$.elements[1].email", is(user3.getEmail())));
    }
    
    @Test
    public void testGetUser() throws Exception {
    	
    	User user = userList.get(2);
    	String userId = String.valueOf(user.getId());
    	
        mockMvc.perform(
        		get("/users/" + userId)
        		.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.id", is(userId)))
        		  .andExpect(jsonPath("$.name", is(user.getName())))
        		  .andExpect(jsonPath("$.email", is(user.getEmail())));
    }
    
    @Test
    public void testGetUser_NotFound() throws Exception {
        mockMvc.perform(
        		get("/users/10000")
        		.with(httpBasic("a","password"))
        		)
                .andExpect(status().is(404));
    }
    
    @Test
    public void testAddUser() throws Exception {

    	mockMvc.perform(
    			post("/users")
    			.with(httpBasic("a","password"))
    			.content(TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk())
    	  .andExpect(jsonPath("$.id").exists())
    	  .andExpect(jsonPath("$.name", is(requestBody.getName())))
		  .andExpect(jsonPath("$.email", is(requestBody.getEmail())));
    	
    	assertEquals(6, userRepository.count());
    	
    }
    
    @Test
    public void testUpdateUser() throws Exception {
    	
    	User userToUpdate = userList.get(1);
    	String userToUpdateId = String.valueOf(userToUpdate.getId());
    	
    	mockMvc.perform(
    			put("/users/" + userToUpdateId)
    			.with(httpBasic("a","password"))
    			.content(TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk())
    	  .andExpect(jsonPath("$.id", is(userToUpdateId)))
    	  .andExpect(jsonPath("$.name", is(requestBody.getName())))
		  .andExpect(jsonPath("$.email", is(requestBody.getEmail())));

    	
    	assertEquals(5, userRepository.count());
    	
    }
    
    @Test
    public void testUpdateUser_NotFound() throws Exception {
    	
        mockMvc.perform(
        		put("/users/10000")
        		.with(httpBasic("a","password"))
        		.content(TestUtils.asJsonString(requestBody))
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		)
                .andExpect(status().is(404));
    }
    
    @Test
    public void testDeleteUser() throws Exception {
    	
    	User userToDelete = userList.get(1);
    	String userToDeleteId = String.valueOf(userToDelete.getId());
    	
    	mockMvc.perform(
    			delete("/users/" + userToDeleteId)
    		    .with(httpBasic("a","password"))
    		    )
    			.andExpect(status().is(204));
    	
    	assertEquals(4, userRepository.count());
    }
    
    @Test
    public void testDeleteUser_NotFound() throws Exception {
    	
    	mockMvc.perform(
    			delete("/users/10000")
    			.with(httpBasic("a","password"))
    			)
    			.andExpect(status().is(404));
    }

}
