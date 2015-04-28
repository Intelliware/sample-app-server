package com.intelliware.sample.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.intelliware.sample.api.model.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long>{


}
