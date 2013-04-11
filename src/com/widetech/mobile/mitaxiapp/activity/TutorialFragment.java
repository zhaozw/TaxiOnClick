package com.widetech.mobile.mitaxiapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class TutorialFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";

	public static TutorialFragment newInstance(String content) {

		TutorialFragment fragment = new TutorialFragment();
		fragment.mContent = content.toString();

		return fragment;
	}

	private String mContent = "???";

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		TextView tutorialText = (TextView) getView().findViewById(
				R.id.textViewTutorial);
		tutorialText.setText(mContent);
		tutorialText
				.setTextSize(10 * getResources().getDisplayMetrics().density);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
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
	}
}
