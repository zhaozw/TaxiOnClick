package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.widetech.mobile.mitaxiapp.activity.util.Config;
import com.widetech.mobile.mitaxiapp.app.Application;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class PositionTaxiActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private Annotation[] sBogota = { new Annotation(new GeoPoint(
			((int) (4.654624729106217 * 1E6)),
			((int) (-74.0587844014467 * 1E6))), "test") };
	private Annotation[][] sRegions = { sBogota };

	// UI References
	private static final String LOG_TAG = "MainActivity";
	private PolarisMapView mMapView;

	private String plateTaxi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_taxi);

		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view_position_taxi);
		this.mMapView.getController().setZoom(15);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			plateTaxi = bundle.getString("taxi_plate");
			int lat = ((int) (((Application) this.getApplication())
					.getLatitude() * 1E6));
			int lon = ((int) (((Application) this.getApplication())
					.getLatitude() * 1E6));
			sBogota[0] = new Annotation(new GeoPoint(lat, lon), plateTaxi);
		}

		// Prepare an alternate pin Drawable
		final Drawable altMarker = MapViewUtils
				.boundMarkerCenterBottom(getResources().getDrawable(
						R.drawable.taxi_map));

		// Prepare the list of Annotation using the alternate Drawable for all
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
		mMapView.setAnnotations(annotations, R.drawable.map_pin_holed_blue_alt);
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

}
