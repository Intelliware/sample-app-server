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
    
    @Test
    public void testGetCompanies_Authorized() throws Exception {
    	
        mockMvc.perform(
        			get("/companies")
        			.with(httpBasic("Company","password"))
        			)
                .andExpect(status().isOk());
        
        mockMvc.perform(
    			get("/companies")
    			.with(httpBasic("CompanyEdit","password"))
    			)
            .andExpect(status().isOk());
        
        mockMvc.perform(
    			get("/companies")
    			.with(httpBasic("CompanyCreate","password"))
    			)
            .andExpect(status().isOk());
    }
    
    @Test
    public void testGetCompanies_NotAuthorized() throws Exception {
    	
        mockMvc.perform(
        			get("/companies")
        			.with(httpBasic("User","password"))
        			)
                .andExpect(status().is(403));
    }
    
    @Test
    public void testGetCompany_Authorized() throws Exception {
    	
    	String companyId = String.valueOf(company.getId());
    	
        mockMvc.perform(
        		get("/companies/" + companyId)
        		.with(httpBasic("Company","password"))
        		)
                .andExpect(status().isOk());
        
        mockMvc.perform(
        		get("/companies/" + companyId)
        		.with(httpBasic("CompanyEdit","password"))
        		)
                .andExpect(status().isOk());
        
        mockMvc.perform(
        		get("/companies/" + companyId)
        		.with(httpBasic("CompanyCreate","password"))
        		)
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetCompany_NotAuthorized() throws Exception {
    	
    	String companyId = String.valueOf(company.getId());
    	
        mockMvc.perform(
        		get("/companies/" + companyId)
        		.with(httpBasic("User","password"))
        		)
                .andExpect(status().is(403));
    }
    
    @Test
    public void testAddCompany_Authorized() throws Exception {
    	
        CompanyVO companyVO = TestUtils.createMyCompanyVO();

    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic("CompanyCreate","password"))
    			.content(TestUtils.asJsonString(companyVO))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk());
    }
    
    @Test
    public void testAddCompany_NotAuthorized() throws Exception {
    	
        CompanyVO companyVO = TestUtils.createMyCompanyVO();

    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic("CompanyEdit","password"))
    			.content(TestUtils.asJsonString(companyVO))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(403));
    	
    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic("Company","password"))
    			.content(TestUtils.asJsonString(companyVO))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(403));
    	
    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic("User","password"))
    			.content(TestUtils.asJsonString(companyVO))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(403));
    }
    
    public void testUpdateCompany_Authorized() throws Exception {
    	
    	String companyToUpdateId = String.valueOf(company.getId());
    	
        CompanyVO company = TestUtils.createMyCompanyVO();

    	mockMvc.perform(
    			put("/companies/" + companyToUpdateId)
    			.with(httpBasic("CompanyEdit","password"))
    			.content(TestUtils.asJsonString(company))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk());
    	
    	mockMvc.perform(
    			put("/companies/" + companyToUpdateId)
    			.with(httpBasic("CompanyCreate","password"))
    			.content(TestUtils.asJsonString(company))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk());
        
    }
    
    public void testUpdateCompany_NotAuthorized() throws Exception {
    	
    	String companyToUpdateId = String.valueOf(company.getId());
    	
        CompanyVO company = TestUtils.createMyCompanyVO();

    	mockMvc.perform(
    			put("/companies/" + companyToUpdateId)
    			.with(httpBasic("Company","password"))
    			.content(TestUtils.asJsonString(company))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(403));
    	
    	mockMvc.perform(
    			put("/companies/" + companyToUpdateId)
    			.with(httpBasic("User","password"))
    			.content(TestUtils.asJsonString(company))
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(403));
        
    }
    
    @Test
    public void testDeleteCompany_Authorized() throws Exception {
    	
    	String companyToDeleteId = String.valueOf(company.getId());
    	
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic("CompanyCreate","password"))
    		    )
    			.andExpect(status().is(204));
    	
    }
    
    @Test
    public void testDeleteCompany_NotAuthorized() throws Exception {
    	
    	String companyToDeleteId = String.valueOf(company.getId());
    	
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic("CompanyEdit","password"))
    		    )
    			.andExpect(status().is(403));
    	
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic("Company","password"))
    		    )
    			.andExpect(status().is(403));
    	
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic("User","password"))
    		    )
    			.andExpect(status().is(403));
    	
    }
    
    
    
    
    
    

}
