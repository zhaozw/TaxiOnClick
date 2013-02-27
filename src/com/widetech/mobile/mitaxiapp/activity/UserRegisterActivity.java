package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.app.SherlockActivity;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.Address;
import com.widetech.mobile.mitaxiapp.object.User;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherRegisterUser;
import com.widetech.mobile.taxionclick.activity.R;
import com.widetech.mobile.tools.GlobalConstants;
import com.widetech.mobile.tools.WideTechTools;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegisterActivity extends SherlockActivity {

	// Values for data at the time of the login attempt.
	private String mName;
	private String mPhone;
	private String mMail;

	// Pattern for validate Email Address
	private Pattern pattern = Patterns.EMAIL_ADDRESS;

	// UI References
	private EditText mNameView;
	private EditText mPhoneView;
	private EditText mEMailView;
	private Button mButtonRegister;
	private ProgressDialog mProgressDialog;

	/**
	 * Keep track of the register task to ensure we can cancel it if requested.
	 */
	private SendRequestTask mRequestTask = null;

	// Connection Detector status network
	private com.widetech.mobile.tools.ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);

		this.mButtonRegister = (Button) findViewById(R.id.register);

		this.mNameView = (EditText) findViewById(R.id.name);
		this.mPhoneView = (EditText) findViewById(R.id.phone);
		this.mEMailView = (EditText) findViewById(R.id.email);

		// Set up the user interaction to manually show or hide the system UI.
		this.mButtonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				try {
					attemptRegister();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});
	}

	protected void attemptRegister() {
		// TODO Auto-generated method stub
		if (this.mRequestTask != null)
			return;

		// Reset Errors
		this.mEMailView.setError(null);
		this.mPhoneView.setError(null);
		this.mNameView.setError(null);

		// Store values at the time of the register attempt.
		this.mName = this.mNameView.getText().toString();
		this.mPhone = this.mPhoneView.getText().toString();
		this.mMail = this.mEMailView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check if name is empty
		if (TextUtils.isEmpty(this.mName)) {
			this.mNameView.setError(getString(R.string.empty_error_field));
			focusView = this.mNameView;
			cancel = true;
		}

		// Check if phone is empty
		if (TextUtils.isEmpty(this.mPhone)) {
			this.mPhoneView.setError(getString(R.string.empty_error_field));
			focusView = this.mPhoneView;
			cancel = true;
		}

		if (TextUtils.isEmpty(this.mMail)) {
			this.mEMailView.setError(getString(R.string.empty_error_field));
			focusView = this.mEMailView;
			cancel = true;
		} else if (!(pattern.matcher(this.mMail).matches())) {
			this.mEMailView.setError(getString(R.string.invalid_mail));
			focusView = this.mEMailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt register and focus the first
			// form field with an error.
			focusView.requestFocus();
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

			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			try {

				this.mProgressDialog = ProgressDialog.show(this, null,
						getString(R.string.dialog_message_sending_register),
						true, false);
				this.mRequestTask = new SendRequestTask();
				this.mRequestTask.execute((Void) null);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	/**
	 * Represents an asynchronous send register to server task
	 */
	public class SendRequestTask extends AsyncTask<Void, String, Boolean> {

		protected Boolean doInBackground(Void... params) {
			boolean status = true;

			// Building Parameters
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Add POST Parameters products

			// Parameter user plattform
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_user),
					GlobalConstants.USER_SERVICE));

			// Parameter pass plattform
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_password),
					GlobalConstants.PASS_SERVICE));

			// Parameter phone number
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_phone_number), mPhone));

			// Parameter Full name
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_full_name), mName));

			// Parameter Email Address
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_email_address), mMail));

			// Parameter Address user
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_address),
					GlobalConstants.ADDRESS_WILDCARD));

			// Parameter sector user
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_sector),
					GlobalConstants.SECTOR_WILDCARD));

			// Parameter note user
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_note),
					GlobalConstants.NOTE_WILDCARD));

			// Parameter imei user
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_imei), WideTechTools
							.getImeiPhone(getApplicationContext())));

			// Create url from request register user
			String urlRegister = new String(getString(R.string.url_taxis)
					+ getString(R.string.method_name_set_customer_record));

			WidetechLogger
					.d("Url del registro del usuario en la plataforma de taxis: "
							+ urlRegister);

			String response = RequestServer.makeHttpRequest(urlRegister,
					GlobalConstants.METHOD_POST, parameters);
			WidetechLogger.d("resultado del stream: " + response);

			if (response == null)
				status = false;
			else {
				try {
					String statusResponse = new XmlFetcherRegisterUser(response)
							.getResultData();

					if ((statusResponse == null) || (statusResponse == ""))
						status = false;
					else {
						if (statusResponse
								.equalsIgnoreCase(GlobalConstants.SUCCESS_REGISTER))
							status = true;
						else
							status = false;

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
			mProgressDialog.dismiss();

			if (success) {

				try {

					User user = new User(mPhone, mName, mName, mMail);
					long stu = FacadeUser.create(user);

					Address address = new Address(
							GlobalConstants.ADDRESS_WILDCARD,
							GlobalConstants.SECTOR_WILDCARD,
							GlobalConstants.NOTE_WILDCARD); 

					long sta = FacadeAddress.create(address);

					boolean statusRecord = false;

					if (stu != -1)
						statusRecord = true;

					if (sta != -1)
						statusRecord = true;

					if (statusRecord) {
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(intent);
						finish();
					} else
						Toast.makeText(getApplicationContext(),
								getString(R.string.error_register_user),
								Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_register_user),
						Toast.LENGTH_LONG).show();
			}
			mProgressDialog = null;
			mRequestTask = null;

		}

		protected void onCancelled() {
			mProgressDialog.dismiss();
			mProgressDialog = null;
			mRequestTask = null;
		}
	}
}