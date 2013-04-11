package com.widetech.mobile.mitaxiapp.activity;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.MapViewUtils;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import com.widetech.mobile.mitaxiapp.object.PositionMobile;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherCancelTaxi;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherPositionTaxi;
import com.widetech.mobile.tools.ConnectionDetector;
import com.widetech.mobile.tools.GlobalConstants;
import com.widetech.mobile.tools.WideTechTools;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class OnlyTaxiActivity extends SherlockMapActivity implements
		OnRegionChangedListener, OnAnnotationSelectionChangedListener {

	private PolarisMapView mMapView;
	private TextView mTextTimeCourse;
	private ProgressDialog mProgressDialogCancelTaxi;

	private String plateTaxi = "";
	private ArrayList<GeoPoint> points;
	private int idService = 0;
	private ArrayList<Mobile> mobileInCourse;

	static int lon = 0;
	static int lat = 0;

	private Timer timer;

	/**
	 * Request to Position Taxi
	 */
	private PositionTaxiTask mPositionTask;

	/**
	 * Request to Cancel Taxi
	 */
	private CancelTaxiTask mCancelTaxiTask;

	// id taxi from search actual position
	private String mobileIdPosition = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_only_taxi);

		idService = ((Application) this.getApplication()).getId();

		findMobileInCourse();

		if (timer == null)
			this.timer = new Timer();

		this.mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view_only_taxi);
		this.mMapView.getController().setZoom(18);
		this.mMapView.setUserTrackingButtonEnabled(true);
		this.mMapView.setOnRegionChangedListenerListener(this);
		this.mMapView.setOnAnnotationSelectionChangedListener(this);

		this.mTextTimeCourse = (TextView) findViewById(R.id.stat_course);
		this.mTextTimeCourse.setTextSize(10 * getResources()
				.getDisplayMetrics().density);

		this.points = new ArrayList<GeoPoint>();

		this.timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				attemptPositionTaxi();

			}
		}, 10000, 60000);
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu sub1 = menu.addSubMenu(getString(R.string.label_ic_cancel_bar))
				.setIcon(R.drawable.ic_content_remove);
		sub1.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		SubMenu sub2 = menu
				.addSubMenu(getString(R.string.label_ic_confirm_bar)).setIcon(
						R.drawable.ic_navigation_accept);
		sub2.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		String title = item.getTitle().toString();

		if (title.equalsIgnoreCase(getString(R.string.label_ic_cancel_bar))) {
			// Cancelar el servicio de Taxi
			attemptCancelService();

		} else if (title
				.equalsIgnoreCase(getString(R.string.label_ic_confirm_bar))) {
			// Confirma que el servicio ha llegado
			questionArrivedTaxi();
		}

		return super.onOptionsItemSelected(item);
	}

	private void findMobileInCourse() {
		// TODO Auto-generated method stub
		try {
			mobileInCourse = FacadeMobile.readMobilesForServiceId(idService);

			if (mobileInCourse != null) {
				mobileIdPosition = mobileInCourse.get(0).getId_position();
				plateTaxi = mobileInCourse.get(0).getPlate();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (timer != null)
			timer.cancel();
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

	protected void attemptCancelService() {
		// TODO Auto-generated method stub
		if (this.mCancelTaxiTask != null)
			return;

		ConnectionDetector connection = new ConnectionDetector(
				getApplicationContext());
		if (!(connection.isConnectingToInternet())) {
			Toast.makeText(this, getString(R.string.error_internet_connection),
					Toast.LENGTH_LONG).show();
			return;
		} else {
			questionCancelTaxi();
		}

	}

	private void questionCancelTaxi() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.message_dialog_cancel_taxi)
				.setTitle(R.string.title_dialog_cancel_taxi)
				.setCancelable(false)
				.setPositiveButton(R.string.label_button_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								cancelTaxi();
								dialog.cancel();
							}
						})
				.setNegativeButton(R.string.label_button_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								dialog.cancel();
							}
						});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private void questionArrivedTaxi() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.message_dialog_arrived_taxi)
				.setTitle(R.string.title_dialog_arrived_taxi)
				.setCancelable(false)
				.setPositiveButton(R.string.label_button_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User confirm arrived taxi
								arrivedTaxi();
								dialog.cancel();
							}
						})
				.setNegativeButton(R.string.label_button_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	protected void arrivedTaxi() {
		// TODO Auto-generated method stub
		try {
			mobileInCourse.get(0).setStatus(1);
			FacadeMobile.updateMobile(mobileInCourse.get(0));
			WideTechTools.displayMessage(
					getString(R.string.message_success_arrived_taxi),
					getApplicationContext(), OnlyTaxiActivity.this);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void cancelTaxi() {
		// TODO Auto-generated method stub

		try {
			this.restricOrientation();
			this.mProgressDialogCancelTaxi = ProgressDialog.show(this, null,
					getString(R.string.dialog_cancel_taxi), true, false);
			this.mCancelTaxiTask = new CancelTaxiTask();
			this.mCancelTaxiTask.execute((Void) null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

	/**
	 * Represents an asynchronous cancel Taxi service to server task
	 */
	public class CancelTaxiTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;

			// Building Parameters
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Add POST parameters cancel Taxi
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_user_request_cancel_taxi),
					GlobalConstants.USER_SERVICE));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_pass_request_cancel_taxi),
					GlobalConstants.PASS_SERVICE));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_service_id_request_cancel_taxi),
							String.valueOf(mobileInCourse.get(0)
									.getId_service())));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_service_int_movil_request_cancel_taxi),
							mobileInCourse.get(0).getMobile()));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_service_date_request_cancel_taxi),
							""));

			for (int i = 0; i < parameters.size(); i++) {
				WidetechLogger.d("parametro: " + parameters.get(i).getName()
						+ " valor: " + parameters.get(i).getValue());
			}

			// Create url from cancel taxi
			String urlCancelTaxi = new String(getString(R.string.url_taxis)
					+ getString(R.string.method_name_cancel_service));

			WidetechLogger.d("Url cancelacion de taxi: " + urlCancelTaxi);

			String response = RequestServer.makeHttpRequest(urlCancelTaxi,
					GlobalConstants.METHOD_POST, parameters);
			WidetechLogger.d("resultado del stream: " + response);

			if (response == null)
				status = false;
			else {
				String statusResponse = new XmlFetcherCancelTaxi(response)
						.getResultData();

				if (TextUtils.isEmpty(statusResponse))
					status = false;
				else {
					WidetechLogger.d("status de la cancelación del taxi: "
							+ statusResponse);

					if (!(GlobalConstants.SUCCESS_CANCEL
							.contentEquals(statusResponse)))
						status = false;
				}
			}

			return status;
		}

		protected void onPostExecute(final Boolean success) {
			mProgressDialogCancelTaxi.dismiss();

			if (success) {
				// Update the taxi
				try {
					mobileInCourse.get(0).setStatus(2);
					WidetechLogger.d("la actualizacion del taxi: "
							+ FacadeMobile.updateMobile(mobileInCourse.get(0)));
					WideTechTools
							.displayMessage(
									getString(R.string.message_success_cancel_servicio),
									getApplicationContext(),
									OnlyTaxiActivity.this);
					finish();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} else {
				WideTechTools.displayMessage(
						getString(R.string.message_error_cancel_taxi),
						getApplicationContext(), OnlyTaxiActivity.this);
			}
		}

		protected void onCancelled() {
			mProgressDialogCancelTaxi.dismiss();
			mProgressDialogCancelTaxi = null;
			mCancelTaxiTask = null;
		}
	}

	public void onBackPressed() {

	}
}
