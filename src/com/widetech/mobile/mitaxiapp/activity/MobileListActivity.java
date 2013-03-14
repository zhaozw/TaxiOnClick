package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import com.actionbarsherlock.app.SherlockListActivity;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.adapters.MobilesAdapter;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import com.widetech.mobile.mitaxiapp.object.PositionMobile;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherCancelTaxi;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherPositionTaxi;
import com.widetech.mobile.tools.ConnectionDetector;
import com.widetech.mobile.tools.GlobalConstants;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MobileListActivity extends SherlockListActivity {

	// Update UI References
	private ArrayList<Mobile> mobilesFounded;
	private ArrayList<Mobile> mobilesInCourse;
	private MobilesAdapter adapter;
	private QuickAction mQuickAction;
	private ProgressDialog mProgressDialogPositionTaxi;
	private ProgressDialog mProgressDialogCancelTaxi;

	// ID's items QuickAction
	private static final int ID_CANCEL = 1;
	private static final int ID_MAP = 2;
	private static final int ID_ARRIVED_TAXI = 3;

	// Item list Taxis selected
	private int selected = -1;

	// id taxi from search actual position
	private String mobileIdPosition = "";

	// plate taxi by actual position
	private String plateTaxi = "";

	/**
	 * Request to Position Taxi
	 */
	private PositionTaxiTask mPositionTask;

	/**
	 * Request to Cancel Taxi
	 */
	private CancelTaxiTask mCancelTaxiTask;

	private boolean servicesInCourse = true;

	private Mobile taxiToCancel = null;

	private int idService = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_list);

		idService = ((Application) this.getApplication()).getId();

		refreshListTaxisInCourse();

		mQuickAction = new QuickAction(this);

		ActionItem cancelItem = new ActionItem(ID_CANCEL,
				getString(R.string.label_quick_action_cancel_mobile),
				getResources().getDrawable(R.drawable.boton_cancel));
		ActionItem mapItem = new ActionItem(ID_MAP,
				getString(R.string.label_quick_action_view_positon_mobile),
				getResources().getDrawable(R.drawable.boton_locate));

		ActionItem arrivedTaxi = new ActionItem(ID_ARRIVED_TAXI,
				getString(R.string.label_quick_action_arrived_taxi),
				getResources().getDrawable(R.drawable.boton_ok));

		mQuickAction.addActionItem(cancelItem);
		mQuickAction.addActionItem(mapItem);
		mQuickAction.addActionItem(arrivedTaxi);

		// setup the action item click listener
		mQuickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						// TODO Auto-generated method stub
						WidetechLogger.d("selected: " + selected);

						if (actionId == ID_MAP) {
							// View mobile in map
							plateTaxi = ((Mobile) adapter.getItem(selected))
									.getPlate();
							mobileIdPosition = ((Mobile) adapter
									.getItem(selected)).getId_position();
							WidetechLogger
									.d("id para la busqueda de posición: "
											+ mobileIdPosition);
							attemptPositionTaxi();
						} else if (actionId == ID_CANCEL) {
							// Cancel Service

							taxiToCancel = (Mobile) adapter.getItem(selected);
							// Cancel Taxi
							attemptCancelService();

						} else if (actionId == ID_ARRIVED_TAXI) {
							taxiToCancel = (Mobile) adapter.getItem(selected);
							attemptArrivedService();
						}
					}
				});
	}

	protected void attemptArrivedService() {
		// TODO Auto-generated method stub

		taxiToCancel.setStatus(1);
		try {
			FacadeMobile.updateMobile(taxiToCancel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		refreshListTaxisInCourse();
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
				this.mProgressDialogPositionTaxi = ProgressDialog.show(this,
						null,
						getString(R.string.dialog_message_find_position_taxi),
						true, false);
				this.mPositionTask = new PositionTaxiTask();
				this.mPositionTask.execute((Void) null);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		selected = position;
		mQuickAction.show(v);
	}

	public void restricOrientation() {
		int current_orientation = getResources().getConfiguration().orientation;
		if (current_orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
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
					((Application) MobileListActivity.this.getApplication())
							.setLatitude(actualPosition.getLatitude());
					((Application) MobileListActivity.this.getApplication())
							.setLongitude(actualPosition.getLongitude());
				}
			}

			return status;
		}

		protected void onPostExecute(final Boolean success) {
			mProgressDialogPositionTaxi.dismiss();

			if (success) {
				Intent intentActualPositionTaxi = new Intent(
						getApplicationContext(), PositionTaxiActivity.class);
				intentActualPositionTaxi.putExtra("taxi_plate", plateTaxi);
				intentActualPositionTaxi.putExtra("id_taxi", mobileIdPosition);
				startActivity(intentActualPositionTaxi);
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.message_error_location_taxi),
						Toast.LENGTH_LONG).show();
			}

			mPositionTask = null;
			mProgressDialogPositionTaxi = null;
		}

		protected void onCancelled() {
			mProgressDialogPositionTaxi.dismiss();
			mPositionTask = null;
			mProgressDialogPositionTaxi = null;
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
							String.valueOf(taxiToCancel.getId_service())));

			parameters
					.add(new BasicNameValuePair(
							getString(R.string.parameter_service_int_movil_request_cancel_taxi),
							taxiToCancel.getMobile()));

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
					taxiToCancel.setStatus(2);
					WidetechLogger.d("la actualizacion del taxi: "
							+ FacadeMobile.updateMobile(taxiToCancel));
					refreshListTaxisInCourse();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.message_error_cancel_taxi),
						Toast.LENGTH_LONG).show();
			}
		}

		protected void onCancelled() {
			mProgressDialogCancelTaxi.dismiss();
			mProgressDialogCancelTaxi = null;
			mCancelTaxiTask = null;
		}
	}

	public void onBackPressed() {
		if (servicesInCourse)
			Toast.makeText(getApplicationContext(),
					getString(R.string.non_close_services_in_course),
					Toast.LENGTH_LONG).show();
		else
			super.onBackPressed();
	}

	public void refreshListTaxisInCourse() {
		// TODO Auto-generated method stub
		mobilesInCourse = new ArrayList<Mobile>();
		try {
			mobilesFounded = FacadeMobile.readMobilesForServiceId(idService);

			if (mobilesFounded != null) {

				for (int i = 0; i < mobilesFounded.size(); i++) {

					if (mobilesFounded.get(i).getStatus() == 0) {
						Mobile m = mobilesFounded.get(i);
						mobilesInCourse.add(m);
					}
				}

				if (mobilesInCourse.size() != 0) {
					this.adapter = new MobilesAdapter(getApplicationContext(),
							this.mobilesFounded);
					this.setListAdapter(this.adapter);
					this.getListView().setFocusable(true);
					this.getListView().setSelection(mobilesFounded.size());
				} else {
					servicesInCourse = false;
					finish();
				}
			} else {
				finish();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}