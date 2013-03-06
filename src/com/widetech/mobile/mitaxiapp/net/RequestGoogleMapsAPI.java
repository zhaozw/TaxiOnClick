package com.widetech.mobile.mitaxiapp.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.location.Address;

@SuppressLint("UseValueOf")
public class RequestGoogleMapsAPI {

	@SuppressLint("UseValueOf")
	public static List<Address> getAddrByWeb(JSONObject jsonObject) {
		List<Address> res = new ArrayList<Address>();
		try {
			JSONArray array = (JSONArray) jsonObject.get("results");
			for (int i = 0; i < array.length(); i++) {
				Double lon = new Double(0);
				Double lat = new Double(0);
				String name = "";
				try {
					lon = array.getJSONObject(i).getJSONObject("geometry")
							.getJSONObject("location").getDouble("lng");

					lat = array.getJSONObject(i).getJSONObject("geometry")
							.getJSONObject("location").getDouble("lat");
					name = array.getJSONObject(i)
							.getString("formatted_address");
					Address addr = new Address(Locale.getDefault());
					addr.setLatitude(lat);
					addr.setLongitude(lon);
					addr.setAddressLine(0, name != null ? name : "");
					res.add(addr);
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();

		}
		return res;
	}
}
