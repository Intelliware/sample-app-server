package com.intelliware.sample.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;
import com.intelliware.sample.vo.PageableListVO;


@RestController
public class CompanyController {

	@Autowired
	private CompanyRepository companyDao;
	
	private Company findCompany(String id) {
		Company company = companyDao.findOne(Long.valueOf(id));
		if (company == null){
			throw new CompanyNotFoundException();
		}
		return company;
	}

	private Company createCompany(CompanyVO inputCompany) {
		Company company = new Company();
		setCompanyAttributes(inputCompany, company);
		return company;
	}

	private void setCompanyAttributes(CompanyVO companyVO, Company company) {
		ContactVO contactVO = companyVO.getContact();
		ContactNameVO contactNameVO = contactVO.getName();
		company.setAddress(companyVO.getAddress());
		company.setPhone(companyVO.getPhone());
		company.setName(companyVO.getName());
		company.setContactEmail(contactVO.getEmail());
		company.setContactFirstName(contactNameVO.getFirst());
		company.setContactLastName(contactNameVO.getLast());
	}
	
	private CompanyVO convertToCompanyVO(Company company) {	
		ContactNameVO contactNameVO = createContactNameVO(company);
		ContactVO contactVO = createContactVO(company, contactNameVO);
		return createCompanyVO(company, contactVO);
	}

	private CompanyVO createCompanyVO(Company company, ContactVO contactVO) {
		CompanyVO companyVO = new CompanyVO();
		companyVO.setId(String.valueOf(company.getId()));
		companyVO.setName(company.getName());
		companyVO.setAddress(company.getAddress());
		companyVO.setPhone(company.getPhone());
		companyVO.setContact(contactVO);
		return companyVO;
	}

	private ContactVO createContactVO(Company company, ContactNameVO contactNameVO) {
		ContactVO contactVO = new ContactVO();
		contactVO.setName(contactNameVO);
		contactVO.setEmail(company.getContactEmail());
		return contactVO;
	}

	private ContactNameVO createContactNameVO(Company company) {
		ContactNameVO contactNameVO = new ContactNameVO();
		contactNameVO.setFirst(company.getContactFirstName());
		contactNameVO.setLast(company.getContactLastName());
		return contactNameVO;
	}

	@Transactional
	@PreAuthorize("hasAnyRole('COMPANY.CREATE', 'COMPANY.EDIT', 'COMPANY')")
	@RequestMapping(value="/companies", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public PageableListVO<CompanyVO> getCompanies() {
		Iterable<Company> companies = companyDao.findAll();
		List<CompanyVO> companyVOList = new ArrayList<CompanyVO>();
		for (Company company : companies){
			companyVOList.add(convertToCompanyVO(company));
		}
		return new PageableListVO<CompanyVO>(companyVOList);
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('COMPANY.CREATE', 'COMPANY.EDIT', 'COMPANY')")
	@RequestMapping(value="/companies/{id}", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public CompanyVO getCompany(@PathVariable String id) {
		Company company = findCompany(id);
		return convertToCompanyVO(company);
	}
	
	@Transactional
	@PreAuthorize("hasRole('COMPANY.CREATE')")
	@RequestMapping(value="/companies", method=RequestMethod.POST, consumes="application/json;charset=UTF-8")
	public CompanyVO addCompany(@RequestBody CompanyVO inputCompany) {
		Company company = createCompany(inputCompany);
		companyDao.save(company);
		return convertToCompanyVO(company);
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('COMPANY.CREATE', 'COMPANY.EDIT')")
	@RequestMapping(value="/companies/{id}", method=RequestMethod.PUT, consumes="application/json;charset=UTF-8")
	public CompanyVO updateCompany(@PathVariable String id, @RequestBody CompanyVO inputCompany) {
		Company company = findCompany(id);
		setCompanyAttributes(inputCompany, company);	
		companyDao.save(company);
		return convertToCompanyVO(company);
	}
	
	@Transactional
	@PreAuthorize("hasRole('COMPANY.CREATE')")
	@RequestMapping(value="/companies/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompany(@PathVariable String id) {
		Company company = findCompany(id);
		companyDao.delete(company);
	}
}
