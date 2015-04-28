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

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.dao.ContactRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.api.model.Contact;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class CompanyControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    private List<Company> companyList = new ArrayList<>();
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        
        companyRepository.deleteAll();
        contactRepository.deleteAll();
        
        Contact contactA = new Contact();
        contactA.setEmail("karyn.porter@stelaecor.com");
        contactA.setFirstName("Karyn");
        contactA.setLastName("Porter");
        contactRepository.save(contactA);
        
        Company company = new Company();
        company.setName("Résumé");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContact(contactA);
        companyList.add(companyRepository.save(company));
        
        company = new Company();
        company.setName("BOILICON");
        company.setAddress("80 Wellington Street, Ottawa, ON K1A 0A2");
        company.setPhone("+1 (893) 432-3827");
        company.setContact(contactA);
        companyList.add(companyRepository.save(company));
    }
    
    @Test
    public void testGetCompanies() throws Exception {
    	
    	Company company1 = this.companyList.get(0);
    	Contact companyContact1 = company1.getContact();
    	Company company2 = this.companyList.get(1);
    	Contact companyContact2 = company2.getContact();
    	
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(company1.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(company1.getName())))
        		  .andExpect(jsonPath("$.elements[0].phone", is(company1.getPhone())))
    		      .andExpect(jsonPath("$.elements[0].contact.email", is(companyContact1.getEmail())))
    		      .andExpect(jsonPath("$.elements[0].contact.name.first", is(companyContact1.getFirstName())))
        		  .andExpect(jsonPath("$.elements[0].contact.name.last", is(companyContact1.getLastName())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(company2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(company2.getName())))
				  .andExpect(jsonPath("$.elements[1].phone", is(company2.getPhone())))
			      .andExpect(jsonPath("$.elements[1].contact.email", is(companyContact2.getEmail())))
			      .andExpect(jsonPath("$.elements[1].contact.name.first", is(companyContact2.getFirstName())))
				  .andExpect(jsonPath("$.elements[1].contact.name.last", is(companyContact2.getLastName())));
    }
    
    @Test
    public void testGetCompany() throws Exception {
    	
    	Company company = this.companyList.get(0);
    	Contact companyContact = company.getContact();
    	String companyId = String.valueOf(company.getId());
    	
        mockMvc.perform(get("/companies/" + companyId))
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.id", is(companyId)))
        		  .andExpect(jsonPath("$.name", is(company.getName())))
        		  .andExpect(jsonPath("$.address", is(company.getAddress())))
        		  .andExpect(jsonPath("$.phone", is(company.getPhone())))
        		  .andExpect(jsonPath("$.contact.email", is(companyContact.getEmail())))
        		  .andExpect(jsonPath("$.contact.name.first", is(companyContact.getFirstName())))
        		  .andExpect(jsonPath("$.contact.name.last", is(companyContact.getLastName())));
    }


}
