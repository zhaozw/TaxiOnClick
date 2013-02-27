package com.widetech.mobile.mitaxiapp.listener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class ListenerLocation implements LocationListener {

	private double latitude = 0;
	private double longitude = 0;
	private Context context;

	public ListenerLocation(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "GPS DESACTIVADO", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "GPS ACTIVADO", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

}
