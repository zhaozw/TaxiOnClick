package com.widetech.mobile.mitaxiapp.dataacesss;

import com.widetech.mobile.mitaxiapp.datastore.MiTaxiAppAdapterDatabase;
import com.widetech.mobile.mitaxiapp.object.User;
import com.widetech.mobile.tools.SQLConstants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DataAccessUser implements DataAccess {

	private MiTaxiAppAdapterDatabase database;

	public DataAccessUser(Context context) {
		this.database = new MiTaxiAppAdapterDatabase(context);
	}

	public <T> long create(T obj) {
		long status = 0;
		User object = (User) obj;
		this.database.open();
		ContentValues valuesUser = this.createContentValuesUser(
				object.getPhone_text(), object.getName_text(),
				object.getLastname_text(), object.getEmail_text());
		status = this.database.getDataBase().insert(
				SQLConstants.TABLE_NAME_USER, null, valuesUser);

		this.database.close();
		return status;
	}

	private ContentValues createContentValuesUser(String phone_text,
			String name_text, String lastname_text, String email_text) {

		ContentValues valuesUser = new ContentValues();
		valuesUser.put(SQLConstants.USER_PHONE, phone_text);
		valuesUser.put(SQLConstants.USER_NAME, name_text);
		valuesUser.put(SQLConstants.USER_LASTNAME, lastname_text);
		valuesUser.put(SQLConstants.USER_EMAIL, email_text);
		return valuesUser;
	}

	@SuppressWarnings("unchecked")
	public <T> T read() {
		// TODO Auto-generated method stub
		User user = null;
		this.database.open();

		Cursor mCursor = this.database.getDataBase().rawQuery(
				SQLConstants.SQL_QUERY_USER, null);
		if (mCursor != null) {
			int count = mCursor.getCount();
			if (count > 0) {
				mCursor.moveToFirst();
				user = new User();
				user.setPhone_text(mCursor.getString(0));
				user.setName_text(mCursor.getString(1));
				user.setLastname_text(mCursor.getString(2));
				user.setEmail_text(mCursor.getString(2));
			}
		}
		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return (T) user;
	}

	public void delete() {

	}

	public <T> T read(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(int id) {
		// TODO Auto-generated method stub

	}
}
