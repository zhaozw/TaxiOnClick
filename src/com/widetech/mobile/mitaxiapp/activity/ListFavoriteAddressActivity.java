package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.widetech.mobile.mitaxiapp.adapters.AddressAdapter;
import com.widetech.mobile.mitaxiapp.facade.FacadeAddress;
import com.widetech.mobile.mitaxiapp.object.Address;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListFavoriteAddressActivity extends SherlockListActivity {

	private AddressAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_favorite_address);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
			adapter = new AddressAdapter(getApplicationContext(), favorites);
			this.setListAdapter(adapter);

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
}
