package com.intelliware.sample.api.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;
import com.intelliware.sample.vo.CompanyVO;
import com.intelliware.sample.vo.ContactNameVO;
import com.intelliware.sample.vo.ContactVO;
import com.intelliware.sample.vo.PageableListVO;


@RestController
public class CompanyController implements IConstants{

	@Autowired
	private CompanyRepository companyDao;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
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
	@RequestMapping(value="/companies", method=RequestMethod.GET, produces=IConstants.jsonUTF8)
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
	@RequestMapping(value="/companies/{id}", method=RequestMethod.GET, produces=IConstants.jsonUTF8)
	public CompanyVO getCompany(@PathVariable String id) {
		Company company = findCompany(id);
		return convertToCompanyVO(company);
	}
	
	@Transactional
	@PreAuthorize("hasRole('COMPANY.CREATE')")
	@RequestMapping(value="/companies", method=RequestMethod.POST, consumes="multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
	public CompanyVO addCompany(@RequestParam(required=false, value="file") MultipartFile file, @RequestParam("data") String companyJsonStr) throws IOException {
		CompanyVO inputCompany = fromJsonString(companyJsonStr, CompanyVO.class);

		Company company = createCompany(inputCompany);
		companyDao.save(company);

		saveFileOnDisk(file);

		return convertToCompanyVO(company);
	}
	
	
	@Transactional
	@PreAuthorize("hasAnyRole('COMPANY.CREATE', 'COMPANY.EDIT')")
	@RequestMapping(value="/companies/{id}", method=RequestMethod.PUT , consumes="multipart/form-data")
    public CompanyVO updateCompany(@PathVariable String id, @RequestParam(required=false, value="file") MultipartFile file, @RequestParam("data") String companyJsonStr) throws IOException {
		CompanyVO inputCompany = fromJsonString(companyJsonStr, CompanyVO.class);
		
		Company company = findCompany(id);
		setCompanyAttributes(inputCompany, company);
		companyDao.save(company);

		saveFileOnDisk(file);
        
        return convertToCompanyVO(company);

    }

	//TODO MZ/DJ this will be removed when we have the database model changes for the file.
	private void saveFileOnDisk(MultipartFile file) throws IOException {

		if (file != null && !file.isEmpty()) {

			byte[] bytes = file.getBytes();
			FileOutputStream fop = null;
			File file1;

			try {

				file1 = new File("c:/temp/" + file.getName());
				fop = new FileOutputStream(file1);

				// if file doesnt exists, then create it
				if (!file1.exists()) {
					file1.createNewFile();
				}

				fop.write(bytes);
				fop.flush();
				fop.close();

				System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fop != null) {
						fop.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(String.format("receive %s, %d",
					file.getOriginalFilename(), file.getBytes().length));
		}
	}
	
	
	@Transactional
	@PreAuthorize("hasRole('COMPANY.CREATE')")
	@RequestMapping(value="/companies/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompany(@PathVariable String id) {
		Company company = findCompany(id);
		companyDao.delete(company);
	}
	
	protected <T> T fromJsonString(String jsonStr, Class<T> clazz) throws IOException {
		return jsonMapper.readValue(jsonStr, clazz);
	}
}
