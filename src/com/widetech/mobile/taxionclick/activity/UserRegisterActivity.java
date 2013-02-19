package com.widetech.mobile.taxionclick.activity;

import com.actionbarsherlock.app.SherlockActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserRegisterActivity extends SherlockActivity {

	private Button mButtonRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);

		this.mButtonRegister = (Button) findViewById(R.id.register);

		// Set up the user interaction to manually show or hide the system UI.
		mButtonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
			}
		});
	}

}
