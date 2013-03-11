package com.widetech.mobile.mitaxiapp.adapters;

import java.util.ArrayList;

import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.object.Address;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Address> address;
	private static LayoutInflater inflater = null;

	public AddressAdapter(Context context, ArrayList<Address> address) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.address = address;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.address.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.address.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.address_item, null);

		TextView textViewAddress = (TextView) vi.findViewById(R.id.AddressItem);
		textViewAddress.setText(this.address.get(position).getAdreess());
		return vi;
	}
}
