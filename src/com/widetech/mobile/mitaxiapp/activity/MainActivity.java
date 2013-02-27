package com.widetech.mobile.mitaxiapp.activity;

import java.util.Locale;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.widetech.mobile.mitaxiapp.activity.util.Config;
import com.widetech.mobile.mitaxiapp.listener.ListenerLocation;
import com.widetech.mobile.taxionclick.activity.R;

public class MainActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private static final String LOG_TAG = "MainActivity";

	private PolarisMapView mMapView;
	private Geocoder mGeodoer;
	private EditText mEditTextAddress;

	// Vars Geolocalization via GPS
	private LocationManager mLocManager;
	private Location mLocation;
	private ListenerLocation mLocationListener;

	// Vars Coords
	private double latitude = 0;
	private double longitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setIcon(R.drawable.logo_top);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);
		this.mMapView.getController().setZoom(17);

		this.mEditTextAddress = (EditText) findViewById(R.id.editTextActualAddress);

		// Init GPS Listener and Listener Location
		this.mLocationListener = new ListenerLocation(getApplicationContext());
		this.mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.mLocation = this.mLocManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		this.mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				30000, 0, this.mLocationListener);

		this.mGeodoer = new Geocoder(getApplicationContext(),
				Locale.getDefault());

		this.obtainLocation();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mMapView.onStart();
		this.obtainLocation();
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.mMapView.onStop();
		this.mLocManager.removeUpdates(this.mLocationListener);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onRegionChanged(PolarisMapView mapView) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChanged");
		}
	}

	@Override
	public void onRegionChangeConfirmed(PolarisMapView mapView) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChangeConfirmed");
		}
	}

	@Override
	public void onAnnotationSelected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationSelected");
		}
		calloutView.setDisclosureEnabled(true);
		calloutView.setClickable(true);
		if (!TextUtils.isEmpty(annotation.getSnippet())) {
			calloutView.setLeftAccessoryView(getLayoutInflater().inflate(
					R.layout.accessory, calloutView, false));
		} else {
			calloutView.setLeftAccessoryView(null);
		}
	}

	@Override
	public void onAnnotationDeselected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationDeselected");
		}
	}

	@Override
	public void onAnnotationClicked(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onAnnotationClicked");
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("").setIcon(R.drawable.ic_action_address)
				.setNumericShortcut('1')
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

	private void obtainLocation() {

		if (this.mLocation != null) {
			this.latitude = this.mLocation.getLatitude();
			this.longitude = this.mLocation.getLongitude();
		} else {
			this.latitude = this.mLocationListener.getLatitude();
			this.longitude = this.mLocationListener.getLongitude();
		}

		Log.i(LOG_TAG, "Coordenadas por el listener - Latitud: " + latitude);
		Log.i(LOG_TAG, "Coordenadas por el listener - Longitud: " + longitude);

		try {
			Log.i(LOG_TAG, "Coordenadas por el listener - Latitud: "
					+ this.mMapView.getMapCenter().getLatitudeE6());
			Log.i(LOG_TAG, "Coordenadas por el listener - Longitud: "
					+ this.mMapView.getMapCenter().getLongitudeE6());

			String address = this.mGeodoer
					.getFromLocation(
							(this.mMapView.getMapCenter().getLatitudeE6() / 1E6),
							(this.mMapView.getMapCenter().getLongitudeE6() / 1E6),
							1).get(0).getAddressLine(0);

			if (address != null && !(address.isEmpty()))
				this.mEditTextAddress.setText(address);
			else

				Toast.makeText(getApplicationContext(),
						"No podemos determinar tu ubicación", Toast.LENGTH_LONG)
						.show();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}