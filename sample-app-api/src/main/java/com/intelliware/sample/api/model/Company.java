package com.intelliware.sample.api.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	//private CompanyContact contact;
	private String name, phone;
	//private Map<String, Object> contact;

	protected Company() {
	}
	
	public Company(String name, String phone, String contactFirstName, String contactLastName, String contactEmail) {
		this.name = name;
		this.phone = phone;
//		Map<String, String> contactName = new HashMap<String, String>();
//		contactName.put("first", contactFirstName);
//		contactName.put("last", contactLastName);
//		this.contact = new HashMap<String, Object>();
//		contact.put("name", contactName);
//		contact.put("email", contactEmail);
		
		//this.contact = new CompanyContact(contactFirstName, contactLastName, contactEmail);
	}
	
	
//	public CompanyContact getContact() {
//		return contact;
//	}
//
//	public void setContact(CompanyContact contact) {
//		this.contact = contact;
//	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
