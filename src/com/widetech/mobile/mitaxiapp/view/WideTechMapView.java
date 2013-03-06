package com.widetech.mobile.mitaxiapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.R;
import com.google.android.maps.MapView;

public class WideTechMapView extends PolarisMapView {

	private boolean mIsUserTrackingButtonEnabled;
	private ImageButton mUserTrackingButton;

	public WideTechMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mIsUserTrackingButtonEnabled) {
			final LayoutParams params = (LayoutParams) mUserTrackingButton
					.getLayoutParams();
			final int spacing = getResources().getDimensionPixelOffset(
					R.dimen.polaris__spacing_normal);
			params.x = w - spacing;
			params.y = spacing + 64;
			// This will obviously start a new layout pass. However there is no
			// risk to fall in an infinite loop here as this code is only called
			// when the size has changed.
			mUserTrackingButton.requestLayout();
		}
	}

	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		// We need to ensure mUserTrackingButton is always on top. As a result,
		// we simply enforce it is brought to front whenever a new child is
		// added. Note that this method is actually called by all other
		// "addView" variants.
		if (mIsUserTrackingButtonEnabled) {
			super.bringChildToFront(mUserTrackingButton);
		}
	}

	public void bringChildToFront(View child) {
		// The code below is part of the trick described in the addView method.
		if (child != mUserTrackingButton) {
			super.bringChildToFront(child);
		}
		if (mIsUserTrackingButtonEnabled) {
			super.bringChildToFront(mUserTrackingButton);
		}
	}

	public boolean isUserTrackingButtonEnabled() {
		return mIsUserTrackingButtonEnabled;
	}

	public void setUserTrackingButtonEnabled(boolean enabled,
			View.OnClickListener listener) {
		if (mIsUserTrackingButtonEnabled != enabled) {
			mIsUserTrackingButtonEnabled = enabled;
			if (enabled) {
				if (mUserTrackingButton == null) {
					mUserTrackingButton = (ImageButton) LayoutInflater.from(
							getContext()).inflate(
							R.layout.polaris__user_tracking_button, null);
					mUserTrackingButton.setOnClickListener(listener);
					// @formatter:off
					mUserTrackingButton
							.setLayoutParams(new MapView.LayoutParams(
									LayoutParams.WRAP_CONTENT, // width
									LayoutParams.WRAP_CONTENT, // height
									0, // x
									0, // y
									LayoutParams.TOP
											| MapView.LayoutParams.RIGHT) // alignment
							);
					// @formatter:on
				}
				addView(mUserTrackingButton);
			} else {
				if (mUserTrackingButton != null) {
					removeView(mUserTrackingButton);
				}
			}
		}
	}
}
