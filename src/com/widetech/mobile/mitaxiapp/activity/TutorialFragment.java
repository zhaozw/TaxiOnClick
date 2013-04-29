package com.widetech.mobile.mitaxiapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public final class TutorialFragment extends Fragment {
	private static final String KEY_CONTENT = "TutorialFragment:Content";
	private static final String KEY_IMAGE_CONTENT = "TutorialFragment:Image";
	private static final String KEY_SCREEN_CONTENT = "TutorialFragment:Screen";

	public static TutorialFragment newInstance(String content,
			int imageResourceId, int screenId) {

		TutorialFragment fragment = new TutorialFragment();
		fragment.mContent = content.toString();
		fragment.mTutorialImageResource = imageResourceId;
		fragment.mScreenImageResource = screenId;

		return fragment;
	}

	private String mContent = "???";
	private int mTutorialImageResource = R.drawable.tutor_1;
	private int mScreenImageResource = R.drawable.screen1;

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		ImageView imageTutorial = (ImageView) getView().findViewById(
				R.id.imageViewResourceTutorial);
		imageTutorial.setImageResource(mTutorialImageResource);
		ImageView imageScreen = (ImageView) getView().findViewById(
				R.id.imageViewScreenTutorial);
		imageScreen.setImageResource(mScreenImageResource);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& (savedInstanceState.containsKey(KEY_CONTENT)
						&& savedInstanceState.containsKey(KEY_IMAGE_CONTENT) && savedInstanceState
							.containsKey(KEY_SCREEN_CONTENT))) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
			mTutorialImageResource = savedInstanceState
					.getInt(KEY_IMAGE_CONTENT);
			mScreenImageResource = savedInstanceState
					.getInt(KEY_SCREEN_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tutorial_fragment, container,
				false);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
		outState.putInt(KEY_IMAGE_CONTENT, mTutorialImageResource);
		outState.putInt(KEY_SCREEN_CONTENT, mScreenImageResource);
	}
}
