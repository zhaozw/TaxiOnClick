package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockListActivity;
import com.widetech.mobile.mitaxiapp.adapters.MobilesAdapter;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import android.os.Bundle;

public class MobileListActivity extends SherlockListActivity {

	private ArrayList<Mobile> mobilesFounded;
	private MobilesAdapter adapter;

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
	}
}