package com.intelliware.sample.api.model;

public class CompanyContactName {

	private String first;
	private String last;

	public CompanyContactName(String contactFirstName, String contactLastName) {
		this.first = contactFirstName;
		this.last = contactLastName;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

}
