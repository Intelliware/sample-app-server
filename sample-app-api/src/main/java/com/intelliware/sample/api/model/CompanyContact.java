package com.intelliware.sample.api.model;

public class CompanyContact {

	private CompanyContactName name;
	private String email;

	public CompanyContact(String contactFirstName, String contactLastName, String contactEmail) {
		this.name = new CompanyContactName(contactFirstName, contactLastName);
		this.email = contactEmail;
	}

	public CompanyContactName getName() {
		return name;
	}

	public void setName(CompanyContactName name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
