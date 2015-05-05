package com.intelliware.sample.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.intelliware.sample.api.model.Company;

public interface CompanyRepository extends CrudRepository<Company, Long>{
}
