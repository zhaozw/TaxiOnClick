package com.widetech.mobile.mitaxiapp.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.PageIndicator;
import android.support.v4.view.ViewPager;

public abstract class BaseHelpActivity extends SherlockFragmentActivity {

	TestFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
}
