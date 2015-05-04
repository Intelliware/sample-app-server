package com.intelliware.sample.api;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;
import com.intelliware.sample.vo.UserVO;

public class TestUtils {
	
	public static MediaType getContentType() {
		return new MediaType(MediaType.APPLICATION_JSON.getType(),
	            MediaType.APPLICATION_JSON.getSubtype(),
	            Charset.forName("utf8"));
	}

	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	} 
	
	public static CompanyVO createMyCompanyVO() {
		CompanyVO company = new CompanyVO();
        company.setName("My Company");
        company.setPhone("+1 (828) 533-2655");
        company.setAddress("200 Adelaide W, Toronto, ON M5H 1W7");
        company.setContact(createMyContactVO());
		return company;
	}

	public static ContactVO createMyContactVO() {
		ContactVO contact = new ContactVO();
        contact.setEmail("my.contact@stelaecor.com");
        contact.setName(createMyContactNameVO());
		return contact;
	}

	public static ContactNameVO createMyContactNameVO() {
		ContactNameVO contactName = new ContactNameVO();
    	contactName.setFirst("My");
    	contactName.setLast("Contact");
		return contactName;
	}
	
	public static UserVO createMyUserVO() {
		UserVO userVO = new UserVO();
		userVO.setName("Elliott");
		userVO.setEmail("pete@dragon.com");
		return userVO;
	}
	
}
