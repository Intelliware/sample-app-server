package com.intelliware.sample.vo;

import java.util.Arrays;

public class CompanyVO {
	
	private String id;
	private String name;
	private String address;
	private String phone;
	private ContactVO contact;
	private byte[] image;

	public CompanyVO() {}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
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

	public byte[] getImage() {
		return (image == null ? null : Arrays.copyOf(image, image.length));
	}

	public void setImage(byte[] image) {
		this.image = (image == null ? null : image.clone());
	}
}
