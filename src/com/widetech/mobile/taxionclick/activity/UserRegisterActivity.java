package com.widetech.mobile.taxionclick.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UserRegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_register, menu);
		return true;
	}
}
