package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.widetech.mobile.mitaxiapp.adapters.AddressAdapter;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.object.Address;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ListFavoriteAddressActivity extends SherlockListActivity {

	private AddressAdapter adapter;
	private View mViewEmptyFavoriteAddress;
	private View mViewFormFavoriteAddress;
	private TextView mTextViewEmptyMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_favorite_address);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mViewEmptyFavoriteAddress = (View) findViewById(R.id.formEmptyFavoriteAddress);
		mViewFormFavoriteAddress = (View) findViewById(R.id.layoutFavoriteAddress);
		mTextViewEmptyMessage = (TextView) findViewById(R.id.textViewMessage);

		this.favoriteAddress();
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

	private void favoriteAddress() {
		// TODO Auto-generated method stub

		try {
			ArrayList<Address> favorites = FacadeAddress.readAllAddress();
			if ((favorites != null) && (favorites.size() != 0)) {
				adapter = new AddressAdapter(getApplicationContext(), favorites);
				this.setListAdapter(adapter);
			} else {
				mTextViewEmptyMessage
						.setText(getString(R.string.no_exists_favorite_address));
				showViewEmptyHistory(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		String address = ((Address) adapter.getItem(position)).getAdreess();
		Intent intent = new Intent(ListFavoriteAddressActivity.this,
				ListFavoriteAddressActivity.class);
		intent.putExtra("address_result", address);
		setResult(Activity.RESULT_OK, intent);
		finish();
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

			mViewEmptyFavoriteAddress.setVisibility(View.VISIBLE);
			mViewEmptyFavoriteAddress.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewEmptyFavoriteAddress
									.setVisibility(show ? View.VISIBLE
											: View.GONE);
						}
					});

			mViewFormFavoriteAddress.setVisibility(View.VISIBLE);
			mViewFormFavoriteAddress.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewFormFavoriteAddress
									.setVisibility(show ? View.GONE
											: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewEmptyFavoriteAddress.setVisibility(show ? View.VISIBLE
					: View.GONE);
			mViewFormFavoriteAddress.setVisibility(show ? View.GONE
					: View.VISIBLE);
		}
	}
}
