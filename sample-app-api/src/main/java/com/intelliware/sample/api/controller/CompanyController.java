package com.intelliware.sample.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;

@RestController
public class CompanyController {

	@Autowired
	private CompanyRepository dao;
	
	@RequestMapping(value="/companies", produces="application/json")
	public Iterable<Company> getCompanies() {
		return dao.findAll();
	}
}
