package com.intelliware.sample.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class CompanyMethodSecurityTest {
	
    private MockMvc mockMvc;
    private String companyId;
    private CompanyVO requestBody = TestUtils.createMyCompanyVO();
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
	
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
        		.addFilters(this.springSecurityFilterChain)
        		.build();
        
        companyRepository.deleteAll();
        
        Company company = new Company();
        company.setName("Résumé");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContactEmail("karyn.porter@stelaecor.com");
        company.setContactFirstName("Karyn");
        company.setContactLastName("Porter");
        
        List<Company> companyList = new ArrayList<>();
        companyList.add(companyRepository.save(company));
        companyId = String.valueOf(companyList.get(0).getId());
        
        requestBody = TestUtils.createMyCompanyVO();
    }
    
	private void performGetCompanies(List<String> usernames, int expectedStatus) throws Exception {
		for (String username : usernames){
			mockMvc.perform(
        			get("/companies")
        			.with(httpBasic(username, "password"))
        			)
                .andExpect(status().is(expectedStatus));
		}
	}
	
	private void performGetCompany(List<String> usernames, int expectedStatus) throws Exception {
		for (String username : usernames) {
			mockMvc.perform(
					get("/companies/" + companyId).with(
							httpBasic(username, "password"))
							)
				.andExpect((status().is(expectedStatus)));
		}
	}
	
    
    private void performAddCompany(List<String> usernames, int expectedStatus) throws Exception {
        for (String username : usernames) {
	    	mockMvc.perform(
	    			post("/companies")
	    			.with(httpBasic(username,"password"))
	    			.param("data", TestUtils.asJsonString(requestBody))
	    			.contentType(MediaType.MULTIPART_FORM_DATA)
	    			.accept(MediaType.APPLICATION_JSON)
	    			)
	    	  .andExpect(status().is(expectedStatus));
        }
	}
    
    private void performUpdatecompany(List<String> usernames, int expectedStatus) throws Exception{	
            for (String username : usernames) {
	        	mockMvc.perform(
	        			put("/companies/" + companyId)
	        			.with(httpBasic(username,"password"))
	        			.param("data", TestUtils.asJsonString(requestBody))
	        			.contentType(MediaType.MULTIPART_FORM_DATA)
	        			.accept(MediaType.APPLICATION_JSON)
	        			)
	        	  .andExpect(status().is(expectedStatus));
            }
    }
    
    private void performDeleteCompany(List<String> usernames, int expectedStatus) throws Exception {
 
    	for (String username : usernames) {
	    	mockMvc.perform(
	    			delete("/companies/" + companyId)
	    		    .with(httpBasic(username,"password"))
	    		    )
	    			.andExpect(status().is(expectedStatus));
    	}
	}
    
    @Test
    public void testGetCompanies_Authorized() throws Exception {
    	performGetCompanies(Arrays.asList("Company", "CompanyEdit", "CompanyCreate"), 200);
    }

    @Test
    public void testGetCompanies_NotAuthorized() throws Exception {
    	performGetCompanies(Arrays.asList("User"), 403);
    }
    
    @Test
    public void testGetCompany_Authorized() throws Exception {  	
    	performGetCompany(Arrays.asList("Company", "CompanyEdit", "CompanyCreate"), 200);
    }

	@Test
    public void testGetCompany_NotAuthorized() throws Exception {
    	performGetCompany(Arrays.asList("User"), 403);
    }
    
    @Test
    public void testAddCompany_Authorized() throws Exception {
        performAddCompany(Arrays.asList("CompanyCreate"), 201);
    }

	@Test
    public void testAddCompany_NotAuthorized() throws Exception {
        performAddCompany(Arrays.asList("CompanyEdit", "Company", "User"), 403);
    }
    
	@Test
    public void testUpdateCompany_Authorized() throws Exception {
        performUpdatecompany(Arrays.asList("CompanyEdit", "CompanyCreate"), 200);
    }
    
	@Test
	public void testUpdateCompany_NotAuthorized() throws Exception {
		performUpdatecompany(Arrays.asList("Company", "User"), 403);
    }
    
    @Test
    public void testDeleteCompany_Authorized() throws Exception {
    	performDeleteCompany(Arrays.asList("CompanyCreate"), 204);
    }

	@Test
    public void testDeleteCompany_NotAuthorized() throws Exception {
		performDeleteCompany(Arrays.asList("CompanyEdit", "Company", "User"), 403);
    }
}
