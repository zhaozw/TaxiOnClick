package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import main.java.com.actionbarsherlock.app.SherlockMapActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.view.MenuItem;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.Address;
import com.widetech.mobile.mitaxiapp.object.User;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherIdService;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherMobile;
import com.widetech.mobile.tools.GlobalConstants;
import com.widetech.mobile.tools.WideTechTools;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ServiceActivity extends SherlockMapActivity {

	// UI References
	private EditText mEditTextTotalTaxis;
	private EditText mEditTextAddress;
	private EditText mEditTextNeighborhood;
	private EditText mEditTextReferencePoint;
	private SeekBar mBarCounterNumberTaxis;
	private ImageButton mButtonObtainService;

	// Views Forms
	private View mServiceFormView;
	private View mStatusView;
	private TextView mServiceMessageStatus;
	private TextView mNameUser;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	private String mAddressForService = "";
	private int mNumberOfTaxis = 0;
	private String mReferenceForService = "";
	private String mNeighborhoodforService = "";

	// Connection Detector status network
	private com.widetech.mobile.tools.ConnectionDetector cd;

	private boolean isNewAddress = false;

	private static final int DEFAULT_PROGRESS_COUNTER_TAXIS = 1;

	/**
	 * Keep track of the service Taxi task to ensure we can cancel it if
	 * requested.
	 */
	private SendServiceTask mServiceTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_service);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.mPreferences = getSharedPreferences(
				GlobalConstants.PREFERENCES_TAG, Context.MODE_PRIVATE);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.mAddressForService = bundle.getString("address_service");
			this.mNeighborhoodforService = bundle
					.getString("neighborhood_service");
		}

		this.mServiceFormView = (View) findViewById(R.id.service_form);
		this.mStatusView = (View) findViewById(R.id.service_status);
		this.mServiceMessageStatus = (TextView) findViewById(R.id.service_status_message);
		this.mNameUser = (TextView) findViewById(R.id.service_name_user);

		this.mEditTextTotalTaxis = (EditText) findViewById(R.id.totalTaxis);
		this.mEditTextAddress = (EditText) findViewById(R.id.address);
		this.mEditTextNeighborhood = (EditText) findViewById(R.id.neighborhood);
		this.mEditTextReferencePoint = (EditText) findViewById(R.id.point);
		this.mBarCounterNumberTaxis = (SeekBar) findViewById(R.id.seekBarTotalTaxis);
		this.mButtonObtainService = (ImageButton) findViewById(R.id.send_info_service);
		this.mEditTextTotalTaxis.setEnabled(false);
		this.mEditTextTotalTaxis.setText("0");
		this.mEditTextAddress.setEnabled(false);

		this.mBarCounterNumberTaxis.setProgress(DEFAULT_PROGRESS_COUNTER_TAXIS);
		this.mEditTextTotalTaxis.setText(Integer
				.toString(DEFAULT_PROGRESS_COUNTER_TAXIS));
		this.mNumberOfTaxis = DEFAULT_PROGRESS_COUNTER_TAXIS;
		this.mBarCounterNumberTaxis
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mEditTextTotalTaxis.setText(Integer.toString(progress));
						mNumberOfTaxis = progress;
						WidetechLogger.d("Total de Taxis en : "
								+ mNumberOfTaxis);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		this.mButtonObtainService.setOnClickListener(onClickButtonGoService);

		this.mEditTextAddress.setText(mAddressForService);
		this.mEditTextNeighborhood.setText(mNeighborhoodforService);

		if (savedInstanceState == null) {
			findDataForAddress();
		}

	}

	private final View.OnClickListener onClickButtonGoService = new View.OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			attemptGoService();
		}
	};

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

	protected void attemptGoService() {
		// TODO Auto-generated method stub

		// Reset Errors
		this.mEditTextNeighborhood.setError(null);
		this.mEditTextReferencePoint.setError(null);

		// Store values at the time of the service attempt.
		this.mNeighborhoodforService = this.mEditTextNeighborhood.getText()
				.toString();
		this.mReferenceForService = this.mEditTextReferencePoint.getText()
				.toString();

		boolean cancel = false;
		View focusView = null;

		// Check if Neighborhood is empty
		if (TextUtils.isEmpty(this.mNeighborhoodforService)) {
			this.mEditTextNeighborhood
					.setError(getString(R.string.empty_error_field));
			focusView = this.mEditTextNeighborhood;
			cancel = true;
		}

		// Check if note is empty
		if (TextUtils.isEmpty(this.mReferenceForService)) {
			this.mEditTextReferencePoint
					.setError(getString(R.string.empty_error_field));
			focusView = this.mEditTextReferencePoint;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt service and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else if (mNumberOfTaxis == 0) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_number_of_taxis),
					Toast.LENGTH_LONG).show();
			// stop executing code by return
			return;
		} else {
			// Check if Internet present
			this.cd = new com.widetech.mobile.tools.ConnectionDetector(
					getApplicationContext());
			if (!this.cd.isConnectingToInternet()) {
				// Internet Connection is not present
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_internet_connection),
						Toast.LENGTH_LONG).show();
				// stop executing code by return
				return;
			}

			WidetechLogger.d("status de la direccion actual " + isNewAddress);
			if (isNewAddress) {
				questionAddNewAddress();
			} else {
				callServiceTaxi();
			}
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mServiceFormView.setVisibility(View.VISIBLE);
			mServiceFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mServiceFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mServiceFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	protected void findDataForAddress() {
		try {
			Address actualAddress = FacadeAddress
					.findDataForAddress(mAddressForService);

			if (actualAddress != null) {
				this.mEditTextReferencePoint.setText(actualAddress.getNote());
				this.mEditTextNeighborhood.setText(actualAddress
						.getNeighborhood());
			} else
				isNewAddress = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void questionAddNewAddress() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.message_dialog_add_new_address)
				.setTitle(R.string.title_dialog_add_new_address)
				.setCancelable(false)
				.setPositiveButton(R.string.label_button_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
								// Add new Address To Storage
								try {
									Address a = new Address(mAddressForService,
											mNeighborhoodforService,
											mReferenceForService);
									FacadeAddress.create(a);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								dialog.cancel();
								callServiceTaxi();
							}
						})
				.setNegativeButton(R.string.label_button_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								dialog.cancel();
								callServiceTaxi();
							}
						});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	protected void callServiceTaxi() {
		// Realize call Service Taxis

		try {
			restricOrientation();
			// Hide form service
			getSupportActionBar().hide();
			User user = FacadeUser.read();
			if (user != null) {
				String name = user.getName_text().split(" ")[0];
				this.mNameUser.setText(name + ":");
			}
			this.mServiceMessageStatus
					.setText(getString(R.string.find_taxi_message));
			this.showProgress(true);
			this.mServiceTask = new SendServiceTask();
			this.mServiceTask.execute((Void) null);

		} catch (Exception e) { // TODO: handle exception
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

	/**
	 * Represents an asynchronous send Service Taxi to server task
	 */
	public class SendServiceTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;
			boolean service = true;
			String response = null;
			int time = 0;

			// Building Parameters
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Add POST Parameters register user

			// Parameter service Taxi
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_user_service),
					GlobalConstants.USER_SERVICE));
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_password_service),
					GlobalConstants.PASS_SERVICE));

			String phoneNumber = "";
			try {
				phoneNumber = FacadeUser.read().getPhone_text();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_phone_number_service),
					phoneNumber));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_type_service), "0"));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_total_taxis_service), String
							.valueOf(mNumberOfTaxis)));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_date_service), ""));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_address_service),
					mAddressForService));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_sector_service),
					mNeighborhoodforService));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_imei_service), WideTechTools
							.getImeiPhone(getApplicationContext())));

			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_note_service),
					mReferenceForService));

			// Create url from request register user
			String urlServiceTaxi = new String(getString(R.string.url_taxis)
					+ getString(R.string.method_name_get_id_service));

			for (int i = 0; i < parameters.size(); i++) {
				WidetechLogger.d("parametro: " + parameters.get(i).getName()
						+ " " + "valor: " + parameters.get(i).getValue());
			}

			WidetechLogger
					.d("Url del registro del usuario en la plataforma de taxis: "
							+ urlServiceTaxi);

			if (isCancelled() == false) {
				response = RequestServer.makeHttpRequest(urlServiceTaxi,
						GlobalConstants.METHOD_POST, parameters);
				WidetechLogger.d("resultado del stream: " + response);
			}

			if (response == null)
				status = false;
			else {
				try {
					String statusResponse = new XmlFetcherIdService(response)
							.getResultData();

					if ((statusResponse == null) || (statusResponse == ""))
						status = false;
					else {
						WidetechLogger.d("id del servicio: " + statusResponse);
						if (!(statusResponse.compareTo("-1") == 0)) {
							((Application) ServiceActivity.this
									.getApplication()).setId(Integer
									.parseInt(statusResponse));
							storeSharedPrefs(Integer.parseInt(statusResponse));
							// Building Parameters
							List<NameValuePair> parametersRequestMobile = new ArrayList<NameValuePair>();

							// Parameter service Taxi
							parametersRequestMobile
									.add(new BasicNameValuePair(
											getString(R.string.parameter_user_get_service_mobile),
											GlobalConstants.USER_SERVICE));
							parametersRequestMobile
									.add(new BasicNameValuePair(
											getString(R.string.parameter_pass_get_service_mobile),
											GlobalConstants.PASS_SERVICE));

							parametersRequestMobile
									.add(new BasicNameValuePair(
											getString(R.string.parameter_id_get_service_mobile),
											statusResponse));

							// Create url from request register user
							String urlServiceGetMobile = new String(
									getString(R.string.url_taxis)
											+ getString(R.string.method_name_get_service_mobile));

							for (int i = 0; i < parametersRequestMobile.size(); i++) {
								WidetechLogger.d("parametro: "
										+ parametersRequestMobile.get(i)
												.getName()
										+ " "
										+ "valor: "
										+ parametersRequestMobile.get(i)
												.getValue());
							}

							WidetechLogger
									.d("Url del registro del usuario en la plataforma de taxis: "
											+ urlServiceGetMobile);

							while (service) {
								if (isCancelled()) {
									service = false;
								}
								WidetechLogger.d("se detiene en: " + service);
								try {
									String responseGetMobile = RequestServer
											.makeHttpRequest(
													urlServiceGetMobile,
													GlobalConstants.METHOD_POST,
													parametersRequestMobile);
									WidetechLogger.d("resultado del stream: "
											+ responseGetMobile);

									if (responseGetMobile == null) {
										status = false;
										service = false;
									} else {
										int statusResponseGetMobile = new XmlFetcherMobile(
												responseGetMobile,
												statusResponse).getResultData();

										WidetechLogger
												.d("Status del response de los moviles: "
														+ statusResponseGetMobile);
										if (statusResponseGetMobile == -1) {
											service = false;
											status = false;
											WidetechLogger
													.d("no encontro servicios");
										} else if (statusResponseGetMobile == 0) {
											service = false;
											WidetechLogger
													.d("encontro servicios");
											status = true;
										} else {
											WidetechLogger
													.d("esperando servicios");
											time = time + 20;
											if (time >= 240) {
												service = false;
												status = false;
											} else {
												Thread.sleep(20000);
											}

											WidetechLogger.d("tiempo " + time);
										}
									}

								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}

						} else {
							status = false;
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					status = false;
				}
			}

			return status;
		}

		protected void onPostExecute(final Boolean success) {
			showProgress(false);
			if (success) {
				if (mNumberOfTaxis == 1) {
					Intent intentShowOnlyMobile = new Intent(
							getApplicationContext(), OnlyTaxiActivity.class);
					startActivity(intentShowOnlyMobile);
					finish();
				} else {
					Intent intentShowMobiles = new Intent(
							getApplicationContext(), MobileListActivity.class);
					startActivity(intentShowMobiles);
					finish();
				}

			} else {
				getSupportActionBar().show();
				WideTechTools.displayMessage(
						getString(R.string.mobile_not_found),
						getApplicationContext(), ServiceActivity.this);
			}
			mServiceTask = null;
		}

		protected void onCancelled() {
			showProgress(false);
			WideTechTools.displayMessage(
					getString(R.string.service_mobile_canceled),
					getApplicationContext(), ServiceActivity.this);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		if ((mServiceTask != null)
				&& (mServiceTask.getStatus() != AsyncTask.Status.FINISHED)) {
			mServiceTask.cancel(true);
		}
	}

	protected void storeSharedPrefs(int value) {
		this.mEditor = mPreferences.edit();
		this.mEditor.putInt(GlobalConstants.NAME_PREFERENCE_LAST_ID_SERVICE,
				value);
		this.mEditor.commit();
	}
}
