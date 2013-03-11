package com.widetech.mobile.mitaxiapp.adapters;

import java.util.ArrayList;

import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MobilesAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Mobile> mobiles;
	private static LayoutInflater inflater = null;

	public MobilesAdapter(Context c, ArrayList<Mobile> mobiles) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.mobiles = mobiles;
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
		return this.mobiles.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return this.mobiles.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup paren) {
		// TODO Auto-generated method stub
		Mobile mobile = this.mobiles.get(position);

		String desc = mobile.getPlate();

		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.mobile_item, null);

		TextView textDesc = (TextView) vi.findViewById(R.id.descMobile);
		TextView textTime = (TextView) vi.findViewById(R.id.timeMobile);

		textDesc.setText("Placa: " + desc);
		textTime.setText("Tiempo de llegada: 5 minutos aprox");

		return vi;
	}
}