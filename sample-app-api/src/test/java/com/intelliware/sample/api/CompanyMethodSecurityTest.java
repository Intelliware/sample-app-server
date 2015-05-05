package com.intelliware.sample.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
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

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class CompanyMethodSecurityTest {
	
    private MockMvc mockMvc;
    private List<Company> companyList = new ArrayList<>();
    private Company company;
    
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
        
        company = new Company();
        company.setName("Résumé");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContactEmail("karyn.porter@stelaecor.com");
        company.setContactFirstName("Karyn");
        company.setContactLastName("Porter");
        companyList.add(companyRepository.save(company));
        company = companyList.get(0);
    }
    

	private void performGetCompanies(String username, ResultMatcher expectedStatus) throws Exception {
		mockMvc.perform(
        			get("/companies")
        			.with(httpBasic(username, "password"))
        			)
                .andExpect(expectedStatus);
	}
	
	private void performGetCompany(String username, ResultMatcher expectedStatus) throws Exception {
		String companyId = String.valueOf(company.getId());
		mockMvc.perform(
        			get("/companies/" + companyId)
        			.with(httpBasic(username, "password"))
        			)
                .andExpect(expectedStatus);
	}
	
    
    private void performAddCompany(String username, ResultMatcher expectedStatus) throws Exception {
        CompanyVO requestBody = TestUtils.createMyCompanyVO();
    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic(username,"password"))
    			.content(TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(expectedStatus);
	}
    
    private void performUpdatecompany(String username, ResultMatcher expectedStatus) throws Exception{
        	String companyId = String.valueOf(company.getId());  	
            CompanyVO requestBody = TestUtils.createMyCompanyVO();
        	mockMvc.perform(
        			put("/companies/" + companyId)
        			.with(httpBasic(username,"password"))
        			.content(TestUtils.asJsonString(requestBody))
        			.contentType(MediaType.APPLICATION_JSON)
        			.accept(MediaType.APPLICATION_JSON)
        			)
        	  .andExpect(expectedStatus);
    }
    
    private void performDeleteCompany(String username, ResultMatcher expectedStatus) throws Exception {
    	String companyToDeleteId = String.valueOf(company.getId());
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic(username,"password"))
    		    )
    			.andExpect(expectedStatus);
	}
    
    @Test
    public void testGetCompanies_Authorized() throws Exception {
    	performGetCompanies("Company", status().isOk());
    	performGetCompanies("CompanyEdit", status().isOk());
        performGetCompanies("CompanyCreate", status().isOk());
    }

    @Test
    public void testGetCompanies_NotAuthorized() throws Exception {
    	performGetCompanies("User", status().is(403));
    }
    
    @Test
    public void testGetCompany_Authorized() throws Exception {  	
    	performGetCompany("Company", status().isOk());
    	performGetCompany("CompanyEdit", status().isOk());
    	performGetCompany("CompanyCreate", status().isOk());
    }

	@Test
    public void testGetCompany_NotAuthorized() throws Exception {
    	performGetCompany("User", status().is(403));
    }
    
    @Test
    public void testAddCompany_Authorized() throws Exception {
        performAddCompany("CompanyCreate", status().isOk());
    }

	@Test
    public void testAddCompany_NotAuthorized() throws Exception {
        performAddCompany("CompanyEdit", status().is(403));
        performAddCompany("Company", status().is(403));
        performAddCompany("User", status().is(403));
    }
    
	@Test
    public void testUpdateCompany_Authorized() throws Exception {
        performUpdatecompany("CompanyEdit", status().isOk());
        performUpdatecompany("CompanyCreate", status().isOk());
    }
    
	@Test
	public void testUpdateCompany_NotAuthorized() throws Exception {
		performUpdatecompany("Company", status().is(403));
		performUpdatecompany("User", status().is(403));
    }
    
    @Test
    public void testDeleteCompany_Authorized() throws Exception {
    	performDeleteCompany("CompanyCreate", status().is(204));
    }

	@Test
    public void testDeleteCompany_NotAuthorized() throws Exception {
		performDeleteCompany("CompanyEdit",status().is(403));
		performDeleteCompany("Company",status().is(403));
		performDeleteCompany("User",status().is(403));
    }
}
