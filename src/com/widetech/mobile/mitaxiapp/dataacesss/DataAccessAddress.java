package com.widetech.mobile.mitaxiapp.dataacesss;

import java.util.ArrayList;
import com.widetech.mobile.mitaxiapp.datastore.MiTaxiAppAdapterDatabase;
import com.widetech.mobile.mitaxiapp.object.Address;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import com.widetech.mobile.tools.SQLConstants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DataAccessAddress implements DataAccess {

	private MiTaxiAppAdapterDatabase database;

	public DataAccessAddress(Context context) {
		this.database = new MiTaxiAppAdapterDatabase(context);
	}

	public <T> long create(T obj) {
		long status = 0;
		Address object = (Address) obj;
		this.database.open();
		ContentValues valuesAddress = this
				.createContentValuesAddress(object.getAdreess(),
						object.getNeighborhood(), object.getNote());
		status = this.database.getDataBase().insert(
				SQLConstants.TABLE_NAME_ADDRESS, null, valuesAddress);
		this.database.close();
		return status;
	}

	private ContentValues createContentValuesAddress(String address,
			String neighborhood, String note) {

		ContentValues valuesSesion = new ContentValues();
		valuesSesion.put(SQLConstants.ADDRESS_DESC, address);
		valuesSesion.put(SQLConstants.ADDRESS_NEIGHBORHOOD, neighborhood);
		valuesSesion.put(SQLConstants.ADDRESS_NOTE, note);
		return valuesSesion;
	}

	public <T> T read() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Address> readAllAddress() {
		ArrayList<Address> listaddress = new ArrayList<Address>();

		this.database.open();

		Cursor mCursor = this.database.getDataBase().query(
				SQLConstants.TABLE_NAME_ADDRESS, SQLConstants.fieldsAddress,
				null, null, null, null, null);

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					Address address = new Address();
					address.setAdreess(mCursor.getString(0));
					address.setNeighborhood(mCursor.getString(1));
					address.setNote(mCursor.getString(2));
					listaddress.add(address);
				} while (mCursor.moveToNext());
			}
		}

		if (mCursor != null)
			mCursor.close();

		this.database.open();
		return listaddress;
	}

	@SuppressWarnings("unchecked")
	public <T> T read(int id) {
		// TODO Auto-generated method stub

		Mobile mobile = null;

		this.database.open();

		Cursor mCursor = null;
		try {

			mCursor = this.database.getDataBase().query(
					SQLConstants.TABLE_NAME_MOBILE, SQLConstants.fieldsMobile,
					SQLConstants.MOBILE_ID_SERVICE + "=" + id, null, null,
					null, null);

		} catch (Exception ex) {
			ex.getMessage();
		}

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				mobile = new Mobile();
				mobile.setPlate(mCursor.getString(1));
				mobile.setMobile(mCursor.getString(2));
			}
		}

		if (mCursor != null)
			mCursor.close();

		this.database.close();

		return (T) mobile;
	}

	public void delete() {
	}

	public void delete(int id) {
		// TODO Auto-generated method stub

	}

}
