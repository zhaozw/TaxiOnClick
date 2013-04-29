package com.widetech.mobile.mitaxiapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.viewpagerindicator.CirclePageIndicator;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.tools.GlobalConstants;

public class HelpActivity extends BaseHelpActivity {

	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	private boolean fromFirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			fromFirst = bundle.getBoolean("fromTutorial");
		}

		this.mPreferences = getSharedPreferences(
				GlobalConstants.PREFERENCES_TAG, Context.MODE_PRIVATE);

		this.getSupportActionBar().setIcon(R.drawable.logo_top);
		this.getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mAdapter = new TutorialFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	protected void storeSharedPrefs(String value) {
		this.mEditor = mPreferences.edit();
		this.mEditor.putString(
				GlobalConstants.NAME_PREFERENCE_FIRST_OPENED_TAG, value);
		this.mEditor.commit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu sub = menu.addSubMenu(getString(R.string.label_ic_close_bar))
				.setIcon(R.drawable.ic_content_remove);
		sub.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		WidetechLogger.d("item: " + item.getItemId());

		int op = item.getItemId();
		switch (op) {
		case 0:
			if (fromFirst) {
				Intent intentHome = new Intent(getApplicationContext(),
						UserRegisterActivity.class);
				startActivity(intentHome);
			}
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}