package com.widetech.mobile.mitaxiapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	protected static final String[] CONTENT = new String[] {
			"Vista 1 del tutorial", "Vista 2 del tutorial",
			"Vista 3 del tutorial", "Vista 4 del tutorial", };

	private int mCount = CONTENT.length;

	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {

		return TutorialFragment.newInstance(CONTENT[position % CONTENT.length]);

	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TestFragmentAdapter.CONTENT[position % CONTENT.length];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}