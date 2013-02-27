package com.widetech.mobile.mitaxiapp.object;

public class PositionMobile {

	private double latitude;
	private double longitude;

	public PositionMobile() {

	}

	public PositionMobile(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
