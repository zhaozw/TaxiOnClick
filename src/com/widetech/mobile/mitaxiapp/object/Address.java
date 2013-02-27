package com.widetech.mobile.mitaxiapp.object;

public class Address {

	private String address;
	private String neighborhood;
	private String note;

	public Address() {
		// TODO Auto-generated constructor stub
	}

	public Address(String address, String neighborhood, String note) {
		this.address = address;
		this.neighborhood = neighborhood;
		this.note = note;
	}

	public void setAdreess(String address) {
		this.address = address;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAdreess() {
		return address;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public String getNote() {
		return note;
	}

}
