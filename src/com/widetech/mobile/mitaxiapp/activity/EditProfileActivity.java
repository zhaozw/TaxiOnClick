package com.widetech.mobile.mitaxiapp.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.object.User;

import android.os.Bundle;
import android.widget.EditText;

public class EditProfileActivity extends SherlockActivity {

	// Update UI References
	private EditText mEditTextName;
	private EditText mEditTextPhone;
	private EditText meEditTextEMail;

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
			break;
		default:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
