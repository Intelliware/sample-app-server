package com.intelliware.sample.vo;


public class CompanyVO {
	
	private String name;
	private String phone;
	private ContactVO contact;

	public CompanyVO() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ContactVO getContact() {
		return contact;
	}

	public void setContact(ContactVO contact) {
		this.contact = contact;
	}
}
