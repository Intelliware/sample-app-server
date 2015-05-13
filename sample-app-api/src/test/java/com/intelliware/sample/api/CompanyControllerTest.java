package com.intelliware.sample.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleAppApiApplication.class)
@WebAppConfiguration
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class CompanyControllerTest {
	
	private static final byte[] SOME_BYTE_CONTENT = "some_byte_content".getBytes();
	private static final byte[] SOME_OLD_BYTE_CONTENT = "some_old_byte_content".getBytes();
	private static final MultipartFile MULTIPART_FILE = new MockMultipartFile("mock new image", SOME_BYTE_CONTENT);
	
	private MediaType contentType = TestUtils.getContentType();

    private MockMvc mockMvc;
    
    private List<Company> companyList = new ArrayList<>();
    private CompanyVO requestBody = TestUtils.createMyCompanyVO();
    private ContactVO requestContact = requestBody.getContact();
    private ContactNameVO requestContactName = requestContact.getName();
    
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
        company.setImage(SOME_OLD_BYTE_CONTENT);
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
    
    @Test
    public void testGetCompanies() throws Exception {
    	
    	Company company1 = this.companyList.get(0);
    	Company company2 = this.companyList.get(1);
    	
        mockMvc.perform(
        			get("/companies")
        			.with(httpBasic("a","password"))
        		)
                .andExpect(status().is(200))
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.elements", hasSize(2)))
        		  .andExpect(jsonPath("$._metadata.totalElements", is(2)))
        		  .andExpect(jsonPath("$.elements[0].id", is(String.valueOf(company1.getId()))))
        		  .andExpect(jsonPath("$.elements[0].name", is(company1.getName())))
        		  .andExpect(jsonPath("$.elements[0].phone", is(company1.getPhone())))
        		  .andExpect(jsonPath("$.elements[0].image", is(encodeToString(SOME_OLD_BYTE_CONTENT))))
    		      .andExpect(jsonPath("$.elements[0].contact.email", is(company1.getContactEmail())))
    		      .andExpect(jsonPath("$.elements[0].contact.name.first", is(company1.getContactFirstName())))
        		  .andExpect(jsonPath("$.elements[0].contact.name.last", is(company1.getContactLastName())))
				  .andExpect(jsonPath("$.elements[1].id", is(String.valueOf(company2.getId()))))
				  .andExpect(jsonPath("$.elements[1].name", is(company2.getName())))
				  .andExpect(jsonPath("$.elements[1].phone", is(company2.getPhone())))
				  .andExpect(jsonPath("$.elements[1].image", IsNull.nullValue()))
			      .andExpect(jsonPath("$.elements[1].contact.email", is(company2.getContactEmail())))
			      .andExpect(jsonPath("$.elements[1].contact.name.first", is(company2.getContactFirstName())))
				  .andExpect(jsonPath("$.elements[1].contact.name.last", is(company2.getContactLastName())));
    }
    
    @Test
    public void testGetCompany() throws Exception {
    	
    	Company company = this.companyList.get(0);
    	String companyId = String.valueOf(company.getId());
    	
        mockMvc.perform(
        		get("/companies/" + companyId)
        		.with(httpBasic("a","password"))
        		)
                .andExpect(status().isOk())
                  .andExpect(content().contentType(contentType))
        		  .andExpect(jsonPath("$.id", is(companyId)))
        		  .andExpect(jsonPath("$.name", is(company.getName())))
        		  .andExpect(jsonPath("$.address", is(company.getAddress())))
        		  .andExpect(jsonPath("$.phone", is(company.getPhone())))
        		  .andExpect(jsonPath("$.image", is(encodeToString(SOME_OLD_BYTE_CONTENT))))
        		  .andExpect(jsonPath("$.contact.email", is(company.getContactEmail())))
        		  .andExpect(jsonPath("$.contact.name.first", is(company.getContactFirstName())))
        		  .andExpect(jsonPath("$.contact.name.last", is(company.getContactLastName())));
    }
    
    @Test
    public void testGetCompany_NotFound() throws Exception {
        mockMvc.perform(
        		get("/companies/10000")
        		.with(httpBasic("a","password"))
        		)
                .andExpect(status().is(404));
    }
    
    @Test
    public void testAddCompany() throws Exception {

    	mockMvc.perform(
    			post("/companies")
    			.with(httpBasic("a","password"))
    			.param("data", TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(201))
    	  .andExpect(jsonPath("$.id").exists())
    	  .andExpect(jsonPath("$.name", is(requestBody.getName())))
		  .andExpect(jsonPath("$.address", is(requestBody.getAddress())))
		  .andExpect(jsonPath("$.phone", is(requestBody.getPhone())))
		  .andExpect(jsonPath("$.contact.email", is(requestContact.getEmail())))
		  .andExpect(jsonPath("$.contact.name.first", is(requestContactName.getFirst())))
		  .andExpect(jsonPath("$.contact.name.last", is(requestContactName.getLast())));
    	
    	assertEquals(3, companyRepository.count());
    }
    
    @Test
    public void testAddCompanyWithFile() throws Exception {


		mockMvc.perform(
    			fileUpload("/companies")
    			.file("file", MULTIPART_FILE.getBytes())
    			.with(httpBasic("a","password"))
    			.param("data", TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().is(201))
    	  .andExpect(jsonPath("$.id").exists())
    	  .andExpect(jsonPath("$.name", is(requestBody.getName())))
		  .andExpect(jsonPath("$.address", is(requestBody.getAddress())))
		  .andExpect(jsonPath("$.phone", is(requestBody.getPhone())))
		  .andExpect(jsonPath("$.image", is(encodeToString(SOME_BYTE_CONTENT))))
		  .andExpect(jsonPath("$.contact.email", is(requestContact.getEmail())))
		  .andExpect(jsonPath("$.contact.name.first", is(requestContactName.getFirst())))
		  .andExpect(jsonPath("$.contact.name.last", is(requestContactName.getLast())));
    	
    	assertEquals(3, companyRepository.count());
    }
    
	@Test
    public void testUpdateCompany() throws Exception {
    	
    	Company companyToUpdate = companyList.get(0);
    	String companyToUpdateId = String.valueOf(companyToUpdate.getId());

    	mockMvc.perform(
    			put("/companies/" + companyToUpdateId)
    			.with(httpBasic("a","password"))
    			.param("data", TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	  .andExpect(status().isOk())
    	  .andExpect(jsonPath("$.id", is(companyToUpdateId)))
    	  .andExpect(jsonPath("$.name", is(requestBody.getName())))
		  .andExpect(jsonPath("$.address", is(requestBody.getAddress())))
		  .andExpect(jsonPath("$.phone", is(requestBody.getPhone())))
		  .andExpect(jsonPath("$.contact.email", is(requestContact.getEmail())))
		  .andExpect(jsonPath("$.contact.name.first", is(requestContactName.getFirst())))
		  .andExpect(jsonPath("$.contact.name.last", is(requestContactName.getLast())));
    	
    	assertEquals(2, companyRepository.count());
    }
    
    @Test
    public void testUpdateCompany_NotFound() throws Exception {
    	
        mockMvc.perform(
        		put("/companies/10000")
        		.with(httpBasic("a","password"))
    			.param("data", TestUtils.asJsonString(requestBody))
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			.accept(MediaType.APPLICATION_JSON)
        		)
                .andExpect(status().is(404));
    }
    
    @Test
    public void testDeleteCompany() throws Exception {
    	
    	Company companyToDelete = this.companyList.get(0);
    	String companyToDeleteId = String.valueOf(companyToDelete.getId());
    	
    	mockMvc.perform(
    			delete("/companies/" + companyToDeleteId)
    		    .with(httpBasic("a","password"))
    		    )
    			.andExpect(status().is(204));
    	
    	assertEquals(1, companyRepository.count());
    }

    @Test
    public void testDeleteCompany_NotFound() throws Exception {
    	
    	mockMvc.perform(
    			delete("/companies/10000")
    			.with(httpBasic("a","password"))
    			)
    			.andExpect(status().is(404));
    }
    
    private Object encodeToString(byte[] byteContent) {
    	return Base64.getEncoder().encodeToString(byteContent);
	}

}
