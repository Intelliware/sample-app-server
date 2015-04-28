package com.intelliware.sample.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.api.model.Contact;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;
import com.intelliware.sample.vo.PageableListVO;

@RestController
public class CompanyController {

	@Autowired
	private CompanyRepository dao;
	
	private CompanyVO createCompanyVO(Company company) {
		
		Contact contact = company.getContact();
		
		ContactNameVO contactNameVO = new ContactNameVO();
		contactNameVO.setFirst(contact.getFirstName());
		contactNameVO.setLast(contact.getLastName());
		
		ContactVO contactVO = new ContactVO();
		contactVO.setName(contactNameVO);
		contactVO.setEmail(contact.getEmail());
		
		CompanyVO companyVO = new CompanyVO();
		companyVO.setId(String.valueOf(company.getId()));
		companyVO.setName(company.getName());
		companyVO.setAddress(company.getAddress());
		companyVO.setPhone(company.getPhone());
		companyVO.setContact(contactVO);
		
		return companyVO;
	}
	
	@RequestMapping(value="/companies", produces="application/json;charset=UTF-8")
	public PageableListVO<CompanyVO> getCompanies() {
		Iterable<Company> companies = dao.findAll();
		List<CompanyVO> companyVOList = new ArrayList<CompanyVO>();
		for (Company company : companies){
			companyVOList.add(createCompanyVO(company));
		}
		return new PageableListVO<CompanyVO>(companyVOList);
	}
	

	@RequestMapping(value="/companies/{id}", produces="application/json;charset=UTF-8")
	public CompanyVO getCompany(@PathVariable String id) {
		Company company = dao.findOne(Long.valueOf(id));
		return createCompanyVO(company);
	}
}
