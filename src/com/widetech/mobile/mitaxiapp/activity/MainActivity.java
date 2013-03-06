package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.activity.util.Config;
import com.widetech.mobile.mitaxiapp.adapters.AddressAdapter;
import com.widetech.mobile.mitaxiapp.components.ui.widgets.SlideMenu;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.listener.ListenerLocation;
import com.widetech.mobile.mitaxiapp.net.RequestGoogleMapsAPI;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.Address;
import com.widetech.mobile.mitaxiapp.view.WideTechMapView;

public class MainActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	// UI References
	private static final String LOG_TAG = "MainActivity";
	private WideTechMapView mMapView;
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
	static List<android.location.Address> address;

	/**
	 * Request to Maps API
	 */
	private FindAddressTask mRequestAddress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setIcon(R.drawable.logo_top);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.mSlideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		this.mMapView = (WideTechMapView) findViewById(R.id.polaris_map_view);
		this.mMapView.getController().setZoom(17);
		// this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		this.mEditTextAddress = (EditText) findViewById(R.id.editTextActualAddress);
		this.mButtonGetTaxi = (Button) findViewById(R.id.button_get_taxi);
		this.mMapView.setUserTrackingButtonEnabled(true, onClickFindPosition);
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

	/**
	 * Click on Button Obtain Actual Position
	 */
	private final View.OnClickListener onClickFindPosition = new View.OnClickListener() {

		public void onClick(View view) {
			obtainLocation();
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		this.mMapView.onStart();
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
		CenterLocation(this.mMapView.getMapCenter());
	}

	@Override
	public void onRegionChangeConfirmed(PolarisMapView mapView) {
		if (Config.INFO_LOGS_ENABLED) {
			Log.i(LOG_TAG, "onRegionChangeConfirmed");
		}
		CenterLocation(this.mMapView.getMapCenter());
		WidetechLogger.d("Ultima latitud: "
				+ this.mMapView.getMapCenter().getLatitudeE6() / 1E6);
		WidetechLogger.d("Ultima longitud: "
				+ this.mMapView.getMapCenter().getLongitudeE6() / 1E6);

		this.latitude = (this.mMapView.getMapCenter().getLatitudeE6() / 1E6);
		this.longitude = (this.mMapView.getMapCenter().getLongitudeE6() / 1E6);

		requestAddressToMarker();
	}

	private void requestAddressToMarker() {
		// TODO Auto-generated method stub
		if (this.mRequestAddress != null)
			return;

		try {

			this.mRequestAddress = new FindAddressTask();
			this.mRequestAddress.execute((Void) null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

			if (latitude == 0)
				this.latitude = this.mMapView.getMapCenter().getLatitudeE6() / 1E6;
			if (longitude == 0)
				this.longitude = this.mMapView.getMapCenter().getLongitudeE6() / 1E6;

			int intlat = (int) (latitude * 1E6);
			int intlon = (int) (longitude * 1E6);

			WidetechLogger.d("latitud inicial: " + intlat);
			WidetechLogger.d("longitud inicial: " + intlon);
			GeoPoint point = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			this.mMapView.getController().setCenter(point);
			CenterLocation(point);

			try {

				this.mRequestAddress = new FindAddressTask();
				this.mRequestAddress.execute((Void) null);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

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

	/**
	 * Represents an asynchronous find address Geocoder to API Google Maps
	 */
	public class FindAddressTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;

			// Building Parameters
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Add GET Parameters Register

			// Parameter latitude and longitude
			parameters.add(new BasicNameValuePair("latlng", latitude + ","
					+ longitude));

			// Parameter sensor FALSE!
			parameters.add(new BasicNameValuePair("sensor", "false"));

			// Create url from request address
			String urlGeocoder = "http://maps.googleapis.com/maps/api/geocode/json";

			for (int i = 0; i < parameters.size(); i++) {
				WidetechLogger.d("parametro: " + parameters.get(i).getName()
						+ " Valor: " + parameters.get(i).getValue());
			}

			WidetechLogger.d("Url del api de Google Maps: " + urlGeocoder);

			String response = RequestServer.makeHttpRequest(urlGeocoder, "GET",
					parameters);
			WidetechLogger.d("resultado del stream: " + response);

			if (response == null)
				status = false;
			else {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					address = RequestGoogleMapsAPI.getAddrByWeb(jsonObject);

					if (address == null || address.size() == 0)
						status = false;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					status = false;
				}
			}

			return status;
		}

		protected void onPostExecute(final Boolean success) {
			if (success) {

				for (int i = 0; i < address.size(); i++) {
					WidetechLogger.d("direcciones: "
							+ address.get(i).getAddressLine(0));
				}
				try {
					String a = address.get(0).getAddressLine(0).split(",")[0]
							.toString();
					mEditTextAddress.setText(a != null ? a : "");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.cannot_determinate_location),
						Toast.LENGTH_LONG).show();
			}

			mRequestAddress = null;
		}

		protected void onCancelled() {
			mRequestAddress = null;
		}
	}

	class InterestingLocations extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;
		private OverlayItem myOverlayItem;

		boolean MoveMap;

		public InterestingLocations(Drawable defaultMarker, int LatitudeE6,
				int LongitudeE6) {
			super(defaultMarker);
			// TODO Auto-generated constructor stub
			this.marker = defaultMarker;
			// create locations of interest
			GeoPoint myPlace = new GeoPoint(LatitudeE6, LongitudeE6);
			myOverlayItem = new OverlayItem(myPlace, "", "");
			locations.add(myOverlayItem);

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return locations.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return locations.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}

	}

	private void CenterLocation(GeoPoint centerGeoPoint) {
		mMapView.getController().animateTo(centerGeoPoint);
		placeMarker(centerGeoPoint.getLatitudeE6(),
				centerGeoPoint.getLongitudeE6());
	};

	private void placeMarker(int markerLatitude, int markerLongitude) {
		Drawable marker = getResources().getDrawable(R.drawable.person_map);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(
				new InterestingLocations(marker, markerLatitude,
						markerLongitude));
	}
}