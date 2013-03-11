package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.app.Application;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

public class PositionTaxiActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private PolarisMapView mMapView;

	private String plateTaxi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_taxi);

		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view_position_taxi);
		this.mMapView.getController().setZoom(17);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			plateTaxi = bundle.getString("taxi_plate");
			int lat = ((int) (((Application) this.getApplication())
					.getLatitude() * 1E6));
			int lon = ((int) (((Application) this.getApplication())
					.getLongitude() * 1E6));
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
			mMapView.setAnnotations(annotations,
					R.drawable.map_pin_holed_blue_alt);
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

}
