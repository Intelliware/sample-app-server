package com.intelliware.sample.vo;

public class ContactVO {
	
	private String email;
	private ContactNameVO name;
	
	public ContactVO() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactNameVO getName() {
		return name;
	}

	public void setName(ContactNameVO name) {
		this.name = name;
	}
}
