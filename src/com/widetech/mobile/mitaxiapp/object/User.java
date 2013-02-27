package com.widetech.mobile.mitaxiapp.object;

public class User {

	private String phone_text;
	private String name_text;
	private String lastname_text;
	private String email_text;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String phone, String name, String lastname, String email) {
		this.phone_text = phone;
		this.name_text = name;
		this.lastname_text = lastname;
		this.email_text = email;
	}

	public void setEmail_text(String email_text) {
		this.email_text = email_text;
	}

	public void setLastname_text(String lastname_text) {
		this.lastname_text = lastname_text;
	}

	public void setName_text(String name_text) {
		this.name_text = name_text;
	}

	public void setPhone_text(String phone_text) {
		this.phone_text = phone_text;
	}

	public String getEmail_text() {
		return email_text;
	}

	public String getLastname_text() {
		return lastname_text;
	}

	public String getName_text() {
		return name_text;
	}

	public String getPhone_text() {
		return phone_text;
	}
}
