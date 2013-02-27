/*
 * 	Application.java
 *
 * Created on January 2, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */
package com.widetech.mobile.mitaxiapp.app;

import android.content.Context;

public class Application extends android.app.Application {

	static Context context;
	
	private int id;
	private double latitude;
	private double longitude;
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
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

	public Application() {
		super();
	}

	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}

	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return super.getApplicationContext();
	}

}
