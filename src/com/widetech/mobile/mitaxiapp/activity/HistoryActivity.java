package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.widetech.mobile.mitaxiapp.card.CardService;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends SherlockActivity {

	private CardUI mCardView;
	private CardStack stack;
	private View mFormEmptyHistory;
	private View mFormStackHistory;
	private TextView mTextViewNoHistory;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		this.getSupportActionBar().setIcon(R.drawable.logo_top);
		this.getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mFormEmptyHistory = (View) findViewById(R.id.formEmptyHistoryService);
		mFormStackHistory = (View) findViewById(R.id.formHistoryService);
		mTextViewNoHistory = (TextView) findViewById(R.id.textViewMessage);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);

		// create a stack
		stack = new CardStack();
		stack.setTitle(getString(R.string.label_ic_history_bar));
		addServicesToStack();
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

	private void addServicesToStack() {
		// TODO Auto-generated method stub

		try {
			ArrayList<Mobile> data = FacadeMobile.readMobileByLastService();

			if ((data != null) & (data.size() != 0)) {

				for (int i = 0; i < data.size(); i++) {
					stack.add(new CardService(
							getString(R.string.title_row_service) + " "
									+ (i + 1), data.get(i).getPlate(), data
									.get(i).getStatus()));
				}
				// add stack to cardView
				mCardView.addStack(stack);
				// draw cards
				mCardView.refresh();
			} else {
				mTextViewNoHistory
						.setText(getString(R.string.no_exists_history));
				showViewEmptyHistory(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showViewEmptyHistory(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mFormEmptyHistory.setVisibility(View.VISIBLE);
			mFormEmptyHistory.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormEmptyHistory.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mFormStackHistory.setVisibility(View.VISIBLE);
			mFormStackHistory.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormStackHistory.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mFormEmptyHistory.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormStackHistory.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}