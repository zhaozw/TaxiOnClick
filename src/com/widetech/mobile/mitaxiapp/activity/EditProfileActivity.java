package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.net.RequestServer;
import com.widetech.mobile.mitaxiapp.object.User;
import com.widetech.mobile.mitaxiapp.xml.XmlFetcherRegisterUser;
import com.widetech.mobile.tools.GlobalConstants;
import com.widetech.mobile.tools.WideTechTools;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends SherlockActivity {

	// Update UI References
	private EditText mEditTextName;
	private EditText mEditTextPhone;
	private EditText meEditTextEMail;
	private ProgressDialog mProgressDialog;

	private String mName;
	private String mPhone;
	private String mMail;

	// Pattern for validate Email Address
	private Pattern pattern = Patterns.EMAIL_ADDRESS;

	/**
	 * Keep track of the register task to ensure we can cancel it if requested.
	 */
	private SendRequestTask mRequestTask = null;

	// Connection Detector status network
	private com.widetech.mobile.tools.ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.mEditTextName = (EditText) findViewById(R.id.name);
		this.mEditTextPhone = (EditText) findViewById(R.id.phone);
		this.meEditTextEMail = (EditText) findViewById(R.id.email);

		if (savedInstanceState == null)
			findDataUser();
	}

	private void findDataUser() {
		// TODO Auto-generated method stub
		try {

			User user = FacadeUser.read();

			if (user != null) {
				this.mEditTextName.setText(user.getName_text());
				this.mEditTextPhone.setText(user.getPhone_text());
				this.meEditTextEMail.setText(user.getEmail_text());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(getString(R.string.label_ic_save_profile))
				.setNumericShortcut('1')
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		char number = item.getNumericShortcut();

		switch (number) {
		case '1':
			attemptRegister();
			break;
		default:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void attemptRegister() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (this.mRequestTask != null)
			return;

		// Reset Errors
		this.meEditTextEMail.setError(null);
		this.mEditTextPhone.setError(null);
		this.mEditTextName.setError(null);

		// Store values at the time of the register attempt.
		this.mName = this.mEditTextName.getText().toString();
		this.mPhone = this.mEditTextPhone.getText().toString();
		this.mMail = this.meEditTextEMail.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check if name is empty
		if (TextUtils.isEmpty(this.mName)) {
			this.mEditTextName.setError(getString(R.string.empty_error_field));
			focusView = this.mEditTextName;
			cancel = true;
		}

		// Check if phone is empty
		if (TextUtils.isEmpty(this.mPhone)) {
			this.mEditTextPhone.setError(getString(R.string.empty_error_field));
			focusView = this.mEditTextPhone;
			cancel = true;
		}

		if (TextUtils.isEmpty(this.mMail)) {
			this.meEditTextEMail
					.setError(getString(R.string.empty_error_field));
			focusView = this.meEditTextEMail;
			cancel = true;
		} else if (!(pattern.matcher(this.mMail).matches())) {
			this.meEditTextEMail.setError(getString(R.string.invalid_mail));
			focusView = this.meEditTextEMail;
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

			// Add POST Parameters Register

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

			// Parameter imei user
			parameters.add(new BasicNameValuePair(
					getString(R.string.parameter_imei), WideTechTools
							.getImeiPhone(getApplicationContext())));

			for (int i = 0; i < parameters.size(); i++) {
				WidetechLogger.d("parametro: " + parameters.get(i).getName()
						+ " valor: " + parameters.get(i).getValue());
			}

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
					FacadeUser.truncateUser();
					User user = new User(mPhone, mName, mName, mMail);
					long stu = FacadeUser.create(user);

					boolean statusRecord = false;

					if (stu != -1)
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
