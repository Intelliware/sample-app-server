package com.intelliware.sample.api.dao;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.repository.CrudRepository;

import com.intelliware.sample.api.model.Company;

@Transactional(value=TxType.MANDATORY)
public interface CompanyRepository extends CrudRepository<Company, Long>{
}
