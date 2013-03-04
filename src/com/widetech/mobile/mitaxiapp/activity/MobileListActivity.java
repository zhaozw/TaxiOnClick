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
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherPositionTaxi;
import com.widetech.mobile.tools.ConnectionDetector;
import com.widetech.mobile.tools.GlobalConstants;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MobileListActivity extends SherlockListActivity {

	// Update UI References
	private ArrayList<Mobile> mobilesFounded;
	private MobilesAdapter adapter;
	private QuickAction mQuickAction;
	private ProgressDialog mProgressDialog;

	// ID's items QuickAction
	private static final int ID_CANCEL = 1;
	private static final int ID_MAP = 2;

	// Item list Taxis selected
	private int selected = -1;

	// id taxi from search actual position
	private String mobileIdPosition = "";

	private String plateTaxi = "";

	private PositionTaxiTask mPositionTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_list);

		if (savedInstanceState == null) {
			// get current id service
			int id = ((Application) this.getApplication()).getId();
			try {
				mobilesFounded = FacadeMobile.readMobilesForServiceId(id);

				if (mobilesFounded != null) {
					this.adapter = new MobilesAdapter(getApplicationContext(),
							this.mobilesFounded);
					this.setListAdapter(this.adapter);
					this.getListView().setFocusable(true);
					this.getListView().setSelection(mobilesFounded.size());
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		mQuickAction = new QuickAction(this);

		ActionItem cancelItem = new ActionItem(ID_CANCEL,
				getString(R.string.label_quick_action_cancel_mobile),
				getResources().getDrawable(R.drawable.ic_cancel_taxi));
		ActionItem mapItem = new ActionItem(ID_MAP,
				getString(R.string.label_quick_action_view_positon_mobile),
				getResources().getDrawable(R.drawable.ic_position_mobile));

		mQuickAction.addActionItem(cancelItem);
		mQuickAction.addActionItem(mapItem);

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
						}
					}
				});
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
				this.mProgressDialog = ProgressDialog.show(this, null,
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

			// Add POST Parameters Register
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
			mProgressDialog.dismiss();

			if (success) {
				Intent intentActualPositionTaxi = new Intent(
						getApplicationContext(), PositionTaxiActivity.class);
				intentActualPositionTaxi.putExtra("taxi_plate", plateTaxi);
				intentActualPositionTaxi.putExtra("id_taxi", mobileIdPosition);
				startActivity(intentActualPositionTaxi);
			}
		}

		protected void onCancelled() {
		}
	}

	/**
	 * Represents an asynchronous cancel Taxi service to server task
	 */
	public class CancelTaxiTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;
			return status;
		}

		protected void onPostExecute(final Boolean success) {
		}

		protected void onCancelled() {
		}
	}
}