package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.activity.util.Config;
import com.widetech.mobile.mitaxiapp.adapters.AddressAdapter;
import com.widetech.mobile.mitaxiapp.components.ui.widgets.SlideMenu;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.listener.ListenerLocation;
import com.widetech.mobile.mitaxiapp.object.Address;

public class MainActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	// UI References
	private static final String LOG_TAG = "MainActivity";
	private PolarisMapView mMapView;
	private EditText mEditTextAddress;
	private SlideMenu mSlideMenu;
	private ListView mListAddress;
	private AddressAdapter mAddressAdapter;
	private Button mButtonGetTaxi;

	private String mAddress;

	// Vars Geolocalization via GPS
	private LocationManager mLocManager;
	private Location mLocation;
	private ListenerLocation mLocationListener;
	private Criteria mCriteria;

	// Vars Coords
	private double latitude = 0;
	private double longitude = 0;

	private ArrayList<Address> currentAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setIcon(R.drawable.logo_top);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.mSlideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view);
		this.mMapView.getController().setZoom(17);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		this.mEditTextAddress = (EditText) findViewById(R.id.editTextActualAddress);
		this.mButtonGetTaxi = (Button) findViewById(R.id.button_get_taxi);

		// Init GPS Listener and Listener Location
		this.mLocationListener = new ListenerLocation(getApplicationContext());
		this.mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.mLocation = this.mLocManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		this.mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, this.mLocationListener);

		try {
			this.currentAddress = FacadeAddress.readAllAddress();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		this.mListAddress = (ListView) findViewById(R.id.main_lv_list_tags);
		this.mAddressAdapter = new AddressAdapter(this, this.currentAddress);
		this.mListAddress.setAdapter(this.mAddressAdapter);

		this.mListAddress.setOnItemClickListener(onItemClickAddressListView);
		this.mButtonGetTaxi.setOnClickListener(onClickButtonGetTaxi);

		this.obtainLocation();
	}

	/**
	 * Click on Address ListView
	 */
	private final AdapterView.OnItemClickListener onItemClickAddressListView = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {

			mEditTextAddress.setText(((Address) mAddressAdapter
					.getItem(position)).getAdreess());
			mSlideMenu.toggle();
		}
	};

	/**
	 * Click on Button Obtain Taxi Service
	 */
	private final View.OnClickListener onClickButtonGetTaxi = new View.OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			attemptService();
		}
	};

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

		menu.add(getString(R.string.label_ic_profile_bar))
				.setIcon(R.drawable.ic_social_person).setNumericShortcut('2')
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(getString(R.string.label_ic_address_bar))
				.setIcon(R.drawable.ic_location_directions)
				.setNumericShortcut('1')
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		char number = item.getNumericShortcut();

		switch (number) {
		case '1':
			mSlideMenu.toggle();
			break;

		case '2':
			Intent intentEditProfile = new Intent(getApplicationContext(),
					EditProfileActivity.class);
			startActivity(intentEditProfile);
			break;

		default:
			mSlideMenu.toggle();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mSlideMenu.isMenuOpen()) {
			mSlideMenu.toggle();
		} else {
			super.onBackPressed();
		}
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

			if (mCriteria == null) {
				mCriteria = new Criteria();
				mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
			}

			mLocManager.getBestProvider(mCriteria, true);

			Log.i(LOG_TAG, "Coordenadas por el mapa - Latitud: "
					+ this.mMapView.getMapCenter().getLatitudeE6());
			Log.i(LOG_TAG, "Coordenadas por el mapa - Longitud: "
					+ this.mMapView.getMapCenter().getLongitudeE6());

			/*
			 * GeoPoint altPoint = new GeoPoint(((int) (this.latitude * 1E6)),
			 * ((int) (this.longitude * 1E6)));
			 * 
			 * this.mMapView.getController().setCenter(altPoint);
			 * this.mMapView.getController().animateTo(altPoint);
			 */

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	protected void attemptService() {

		// Reset Errors
		this.mEditTextAddress.setError(null);

		// Store values at the time of the service attempt.
		this.mAddress = this.mEditTextAddress.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check if name is empty
		if (TextUtils.isEmpty(this.mAddress)) {
			this.mEditTextAddress
					.setError(getString(R.string.empty_error_field));
			focusView = this.mEditTextAddress;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt register and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {

			Intent intentSolciteService = new Intent(getApplicationContext(),
					ServiceActivity.class);
			intentSolciteService.putExtra("address_service", mAddress);
			startActivity(intentSolciteService);

		}
	}
}