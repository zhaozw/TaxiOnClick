package com.widetech.mobile.mitaxiapp.object;

public class Mobile {

	private String plate;
	private String mobile;
	private int id_service;
	private String id_position;
	private int status;
	
	public Mobile() {
		// TODO Auto-generated constructor stub
	}
	

	public void setId_service(int id_service) {
		this.id_service = id_service;
	}
	public void setId_position(String id_position) {
		this.id_position = id_position;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPlate() {
		return plate;
	}
	public String getMobile() {
		return mobile;
	}
	public String getId_position() {
		return id_position;
	}
	public int getId_service() {
		return id_service;
	}
	
	public int getStatus() {
		return status;
	}

}
