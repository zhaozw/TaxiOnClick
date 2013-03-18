package com.widetech.mobile.mitaxiapp.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends SherlockActivity {

	// Ui References
	private TextView mTextViewDisclamer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		getSupportActionBar().setIcon(R.drawable.logo_top);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.mTextViewDisclamer = (TextView) findViewById(R.id.textViewUserTerms);
		this.mTextViewDisclamer.setOnClickListener(onClickWebLicense);
		this.mTextViewDisclamer.setOnTouchListener(onTouchLink);
	}

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

	/**
	 * Click open license in Web Browser
	 */
	private final View.OnClickListener onClickWebLicense = new View.OnClickListener() {

		public void onClick(View view) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(getString(R.string.link_to_web_license)));
			startActivity(intent);
		}
	};

	private final View.OnTouchListener onTouchLink = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// Change color
				mTextViewDisclamer.setTextColor(0xffffd90e);
			} else if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				// Change it back
				mTextViewDisclamer.setTextColor(0xFF000000);
			}
			return false;
		}
	};
}
