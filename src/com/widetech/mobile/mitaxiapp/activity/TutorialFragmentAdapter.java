package com.widetech.mobile.mitaxiapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TutorialFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	protected static final String[] CONTENT = new String[] {
			"Vista 1 del tutorial", "Vista 2 del tutorial",
			"Vista 3 del tutorial", "Vista 4 del tutorial", };

	protected static final int[] MESSAGES_ID = new int[] { R.drawable.tutor_1,
			R.drawable.tutor_2, R.drawable.tutor_3, R.drawable.tutor_4 };

	protected final int[] SCREENS_ID = new int[] { R.drawable.screen1,
			R.drawable.screen2, R.drawable.screen3, R.drawable.screen4 };

	private int mCount = CONTENT.length;

	public TutorialFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {

		return TutorialFragment.newInstance(CONTENT[position % CONTENT.length],
				MESSAGES_ID[position % MESSAGES_ID.length], SCREENS_ID[position
						% SCREENS_ID.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TutorialFragmentAdapter.CONTENT[position % CONTENT.length];
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