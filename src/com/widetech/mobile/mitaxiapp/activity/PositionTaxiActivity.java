package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.view.MenuItem;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.PositionMobile;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherPositionTaxi;
import com.widetech.mobile.tools.ConnectionDetector;
import com.widetech.mobile.tools.GlobalConstants;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class PositionTaxiActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private PolarisMapView mMapView;

	private String plateTaxi = "";

	private ArrayList<GeoPoint> points;

	static int lon = 0;
	static int lat = 0;

	private Timer timer;

	/**
	 * Request to Position Taxi
	 */
	private PositionTaxiTask mPositionTask;

	// id taxi from search actual position
	private String mobileIdPosition = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_taxi);

		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (timer == null)
			this.timer = new Timer();

		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view_position_taxi);
		this.mMapView.getController().setZoom(18);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		this.points = new ArrayList<GeoPoint>();

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			plateTaxi = bundle.getString("taxi_plate");
			mobileIdPosition = bundle.getString("id_taxi");
			lat = ((int) (((Application) this.getApplication()).getLatitude() * 1E6));
			lon = ((int) (((Application) this.getApplication()).getLongitude() * 1E6));
		}

		createPoints();

		this.timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				WidetechLogger.d("holaa perras");
				attemptPositionTaxi();

			}
		}, 60000, 60000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (timer != null)
			timer.cancel();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int op = item.getItemId();
		switch (op) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void createPoints() {

		Annotation[] sBogota = { new Annotation(new GeoPoint(lat, lon),
				plateTaxi) };
		Annotation[][] sRegions = { sBogota };

		// Prepare an alternate pin Drawable
		final Drawable altMarker = MapViewUtils
				.boundMarkerCenterBottom(getResources().getDrawable(
						R.drawable.taxi_map));

		// Prepare the list of Annotation using the alternate Drawable for
		// all
		// Annotation located in France
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		for (Annotation[] region : sRegions) {
			for (Annotation annotation : region) {
				if (region == sBogota) {
					annotation.setMarker(altMarker);
				}
				annotations.add(annotation);
			}
		}
		mMapView.setAnnotations(annotations, R.drawable.taxi_map);

		if (points.size() != 0)
			points.clear();

		points.add(this.mMapView.getMapCenter());
		points.add(new GeoPoint(lat, lon));

		boundingPoints();
	}

	protected void attemptPositionTaxi() {
		// TODO Auto-generated method stub

		// Reset errors.
		if (this.mPositionTask != null) {
			return;
		}

		ConnectionDetector connection = new ConnectionDetector(
				getApplicationContext());
		if (!(connection.isConnectingToInternet())) {
			Toast.makeText(this, getString(R.string.error_internet_connection),
					Toast.LENGTH_LONG).show();
			return;
		} else {

			try {
				this.restricOrientation();
				this.mPositionTask = new PositionTaxiTask();
				this.mPositionTask.execute((Void) null);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	public void restricOrientation() {
		int current_orientation = getResources().getConfiguration().orientation;
		if (current_orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mMapView.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.mMapView.onStop();
	}

	@Override
	public void onRegionChanged(PolarisMapView mapView) {
		WidetechLogger.d("onRegionChanged");
	}

	@Override
	public void onRegionChangeConfirmed(PolarisMapView mapView) {
		WidetechLogger.d("onRegionChangeConfirmed");
	}

	@Override
	public void onAnnotationSelected(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {

		WidetechLogger.d("onAnnotationSelected");
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
		WidetechLogger.d("onAnnotationDeselected");
	}

	@Override
	public void onAnnotationClicked(PolarisMapView mapView,
			MapCalloutView calloutView, int position, Annotation annotation) {
		WidetechLogger.d("onAnnotationClicked");

	}

	private void boundingPoints() {

		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;

		for (GeoPoint item : points) {

			int lat = item.getLatitudeE6();
			int lon = item.getLongitudeE6();

			maxLat = Math.max(lat, maxLat);
			minLat = Math.min(lat, minLat);
			maxLon = Math.max(lon, maxLon);
			minLon = Math.min(lon, minLon);
		}

		this.mMapView.getController().zoomToSpan(Math.abs(maxLat - minLat),
				Math.abs(maxLon - minLon));
		this.mMapView.getController().animateTo(
				new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));

	}

	/**
	 * Represents an asynchronous view position Taxi to server task
	 */
	public class PositionTaxiTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;

			// Building Parameters
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Add POST Parameters Position Taxi
			// Parameter user request taxi
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_user_request_position_taxi),
					GlobalConstants.USER_TAXI));
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_pass_request_position_taxi),
					GlobalConstants.PASS_TAXI));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_custom_id_request_position_taxi),
							"16367"));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_service_id_request_position_taxi),
							mobileIdPosition));

			// Create url from actual position taxi
			String urlPositionTaxi = new String(
					getString(R.string.url_service_request_position)
							+ getString(R.string.method_name_request_position_taxi));

			WidetechLogger.d("Url de la posicion actual del taxi "
					+ urlPositionTaxi);

			String response = RequestServer.makeHttpRequest(urlPositionTaxi,
					GlobalConstants.METHOD_POST, parameters);
			WidetechLogger.d("resultado del stream: " + response);

			if (response == null)
				status = false;
			else {
				PositionMobile actualPosition = new XmlFetcherPositionTaxi(
						response).getResultData();

				if (actualPosition == null)
					status = false;
				else {
					WidetechLogger.d("latitud del movil: "
							+ actualPosition.getLatitude());
					WidetechLogger.d("longitud del movil: "
							+ actualPosition.getLongitude());

					lat = (int) (actualPosition.getLatitude() * 1E6);
					lon = (int) (actualPosition.getLongitude() * 1E6);
				}
			}

			return status;
		}

		protected void onPostExecute(final Boolean success) {

			if (success) {

				createPoints();
			}
			mPositionTask = null;
		}

		protected void onCancelled() {
			mPositionTask = null;
		}
	}
}
