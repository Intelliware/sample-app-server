package com.intelliware.sample.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
public class CompanyControllerTest {
	
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
    
    private List<Company> companyList = new ArrayList<>();
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        
        companyRepository.deleteAll();
        
        Company company = new Company();
        company.setName("Résumé");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContactEmail("karyn.porter@stelaecor.com");
        company.setContactFirstName("Karyn");
        company.setContactLastName("Porter");
        companyList.add(companyRepository.save(company));
        
        company = new Company();
        company.setName("BOILICON");
        company.setAddress("80 Wellington Street, Ottawa, ON K1A 0A2");
        company.setPhone("+1 (893) 432-3827");
        company.setContactEmail("mark.zuckerberg@stelaecor.com");
        company.setContactFirstName("Mark");
        company.setContactLastName("Zuckerberg");
        companyList.add(companyRepository.save(company));
    }
    
	private CompanyVO createMyCompanyVO() {
		CompanyVO company = new CompanyVO();
        company.setName("My Company");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContact(createMyContactVO());
		return company;
	}

	private ContactVO createMyContactVO() {
		ContactVO contact = new ContactVO();
        contact.setEmail("my.contact@stelaecor.com");
        contact.setName(createMyContactNameVO());
		return contact;
	}

	private ContactNameVO createMyContactNameVO() {
		ContactNameVO contactName = new ContactNameVO();
    	contactName.setFirst("My");
    	contactName.setLast("Contact");
		return contactName;
	}
    
    @Test
    public void testGetCompanies() throws Exception {
    	
    	Company company1 = this.companyList.get(0);
    	Company company2 = this.companyList.get(1);
    	
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(company1.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(company1.getName())))
        		  .andExpect(jsonPath("$.elements[0].phone", is(company1.getPhone())))
    		      .andExpect(jsonPath("$.elements[0].contact.email", is(company1.getContactEmail())))
    		      .andExpect(jsonPath("$.elements[0].contact.name.first", is(company1.getContactFirstName())))
        		  .andExpect(jsonPath("$.elements[0].contact.name.last", is(company1.getContactLastName())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(company2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(company2.getName())))
				  .andExpect(jsonPath("$.elements[1].phone", is(company2.getPhone())))
			      .andExpect(jsonPath("$.elements[1].contact.email", is(company2.getContactEmail())))
			      .andExpect(jsonPath("$.elements[1].contact.name.first", is(company2.getContactFirstName())))
				  .andExpect(jsonPath("$.elements[1].contact.name.last", is(company2.getContactLastName())));
    }
    
    @Test
    public void testGetCompany() throws Exception {
    	
    	Company company = this.companyList.get(0);
    	String companyId = String.valueOf(company.getId());
    	
        mockMvc.perform(get("/companies/" + companyId))
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.id", is(companyId)))
        		  .andExpect(jsonPath("$.name", is(company.getName())))
        		  .andExpect(jsonPath("$.address", is(company.getAddress())))
        		  .andExpect(jsonPath("$.phone", is(company.getPhone())))
        		  .andExpect(jsonPath("$.contact.email", is(company.getContactEmail())))
        		  .andExpect(jsonPath("$.contact.name.first", is(company.getContactFirstName())))
        		  .andExpect(jsonPath("$.contact.name.last", is(company.getContactLastName())));
    }
    
    @Test
    public void testGetCompany_NotFound() throws Exception {
        mockMvc.perform(get("/companies/10000"))
                .andExpect(status().is(404));
    }
    
    @Test
    public void testAddCompany() throws Exception {
    	
        CompanyVO company = createMyCompanyVO();
        ContactVO contact = company.getContact();
        ContactNameVO contactName = contact.getName();

    	mockMvc.perform(post("/companies")
    	  .content(asJsonString(company))
    	  .contentType(MediaType.APPLICATION_JSON)
    	  .accept(MediaType.APPLICATION_JSON))
    	  .andExpect(jsonPath("$.id").exists())
    	  .andExpect(jsonPath("$.name", is(company.getName())))
		  .andExpect(jsonPath("$.address", is(company.getAddress())))
		  .andExpect(jsonPath("$.phone", is(company.getPhone())))
		  .andExpect(jsonPath("$.contact.email", is(contact.getEmail())))
		  .andExpect(jsonPath("$.contact.name.first", is(contactName.getFirst())))
		  .andExpect(jsonPath("$.contact.name.last", is(contactName.getLast())));
    	
    	assertEquals(3, companyRepository.count());
    }
    
    @Test
    public void testUpdateCompany() throws Exception {
    	
    	Company companyToUpdate = this.companyList.get(0);
    	String companyToUpdateId = String.valueOf(companyToUpdate.getId());
    	
        CompanyVO company = createMyCompanyVO();
        ContactVO contact = company.getContact();
        ContactNameVO contactName = contact.getName();

    	mockMvc.perform(put("/companies/" + companyToUpdateId)
    	  .content(asJsonString(company))
    	  .contentType(MediaType.APPLICATION_JSON)
    	  .accept(MediaType.APPLICATION_JSON))
    	  .andExpect(jsonPath("$.id", is(companyToUpdateId)))
    	  .andExpect(jsonPath("$.name", is(company.getName())))
		  .andExpect(jsonPath("$.address", is(company.getAddress())))
		  .andExpect(jsonPath("$.phone", is(company.getPhone())))
		  .andExpect(jsonPath("$.contact.email", is(contact.getEmail())))
		  .andExpect(jsonPath("$.contact.name.first", is(contactName.getFirst())))
		  .andExpect(jsonPath("$.contact.name.last", is(contactName.getLast())));
    	
    	assertEquals(2, companyRepository.count());
    	
        
    }
    
    @Test
    public void testUpdateCompany_NotFound() throws Exception {
        CompanyVO company = createMyCompanyVO();
    	
        mockMvc.perform(put("/companies/10000")
        		.content(asJsonString(company))
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
    
//    @Test
//    public void testDeleteCompany() throws Exception {
//    	
//    	Company companyToDelete = this.companyList.get(0);
//    	String companyToDeleteId = String.valueOf(companyToDelete.getId());
//    	
//    }




}
