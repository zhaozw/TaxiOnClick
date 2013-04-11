package com.widetech.mobile.mitaxiapp.adapters;

import java.util.ArrayList;
import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServicesAdapter extends BaseAdapter {

	private ArrayList<Mobile> data;
	private Context context;
	int numberOfService = 0;

	public ServicesAdapter(ArrayList<Mobile> data, Context context) {
		this.data = data;
		this.context = context;
		this.numberOfService = this.data.size();
	}

	public void setData(ArrayList<Mobile> data) {
		this.data = data;
	}

	/**
	 * @see android.widget.BaseAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * @see android.widget.BaseAdapter#getItem(int)
	 */
	@Override
	public Mobile getItem(int position) {
		return data.get(position);
	}

	/**
	 * @see android.widget.BaseAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class PlaceHolder {
		TextView tvTitle;
		TextView tvDescription;

		public static PlaceHolder generate(View convertView) {
			PlaceHolder placeHolder = new PlaceHolder();
			placeHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.service_row_tv_title);
			placeHolder.tvDescription = (TextView) convertView
					.findViewById(R.id.service_row_tv_description);
			return placeHolder;
		}

	}

	/**
	 * @see android.widget.BaseAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Mobile item = data.get(position);
		PlaceHolder placeHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.service_row, null);
			placeHolder = PlaceHolder.generate(convertView);
			convertView.setTag(placeHolder);
		} else {
			placeHolder = (PlaceHolder) convertView.getTag();
		}

		placeHolder.tvTitle.setText(context
				.getString(R.string.title_row_service)
				+ " "
				+ (data.size() - position));
		placeHolder.tvDescription.setText(context
				.getString(R.string.desc_plate_taxi) + " " + item.getPlate());

		return convertView;
	}

}
