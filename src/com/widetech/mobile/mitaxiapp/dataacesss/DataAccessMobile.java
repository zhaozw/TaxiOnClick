/*
 * DataAccessSesion.java
 *
 * Created on December 11, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.mitaxiapp.dataacesss;

import java.util.ArrayList;
import java.util.Vector;
import com.widetech.mobile.mitaxiapp.datastore.MiTaxiAppAdapterDatabase;
import com.widetech.mobile.mitaxiapp.object.Mobile;
import com.widetech.mobile.tools.SQLConstants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DataAccessMobile implements DataAccess {

	private MiTaxiAppAdapterDatabase database;

	public DataAccessMobile(Context context) {
		this.database = new MiTaxiAppAdapterDatabase(context);
	}

	/* the row ID of the newly inserted row, or -1 if an error occurred */

	public <T> long create(T obj) {
		long status = 0;
		Mobile object = (Mobile) obj;
		this.database.open();
		ContentValues valuesSesion = this.createContentValuesMobile(
				object.getId_service(), object.getPlate(), object.getMobile(),
				object.getId_position(), object.getStatus());
		status = this.database.getDataBase().insert(
				SQLConstants.TABLE_NAME_MOBILE, null, valuesSesion);
		this.database.close();
		return status;
	}

	private ContentValues createContentValuesMobile(int id_service,
			String number_plate, String description, String id_position,
			int status) {

		ContentValues valuesSesion = new ContentValues();
		valuesSesion.put(SQLConstants.MOBILE_ID_SERVICE, id_service);
		valuesSesion.put(SQLConstants.MOBILE_NUMBER_PLATE, number_plate);
		valuesSesion.put(SQLConstants.MOBILE_DESC, description);
		valuesSesion.put(SQLConstants.MOBILE_ID_POSITION, id_position);
		valuesSesion.put(SQLConstants.MOBILE_STATUS, status);
		return valuesSesion;
	}

	public <T> T read() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Mobile> readAllMobiles() {
		ArrayList<Mobile> mobiles = new ArrayList<Mobile>();

		this.database.open();
		Cursor mCursor = this.database.getDataBase().query(
				SQLConstants.TABLE_NAME_MOBILE, SQLConstants.fieldsMobile,
				null, null, null, null, null);

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					Mobile mobile = new Mobile();

					mobiles.add(mobile);
				} while (mCursor.moveToNext());
			}
		}
		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return mobiles;
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
				mobile.setId_service(mCursor.getInt(0));
				mobile.setPlate(mCursor.getString(1));
				mobile.setMobile(mCursor.getString(2));
				mobile.setId_position(mCursor.getString(3));
				mobile.setStatus(mCursor.getInt(4));
			}
		}
		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return (T) mobile;
	}

	public <T> long update(T obj) {
		long status = 0;

		Mobile mobile = (Mobile) obj;
		this.database.open();
		ContentValues valuesMobileUpdate = this.createContentValuesMobile(
				mobile.getId_service(), mobile.getPlate(), mobile.getMobile(),
				mobile.getId_position(), mobile.getStatus());

		status = this.database.getDataBase().update(
				SQLConstants.TABLE_NAME_MOBILE,
				valuesMobileUpdate,
				SQLConstants.MOBILE_ID_SERVICE + "=" + mobile.getId_service()
						+ " AND " + SQLConstants.MOBILE_NUMBER_PLATE + "="
						+ "'" + mobile.getPlate() + "'", null);
		this.database.close();
		return status;
	}

	public ArrayList<Mobile> readMobilesForIdService(int id) {
		ArrayList<Mobile> services = new ArrayList<Mobile>();

		this.database.open();
		Cursor mCursor = this.database.getDataBase().query(
				SQLConstants.TABLE_NAME_MOBILE, SQLConstants.fieldsMobile,
				SQLConstants.MOBILE_ID_SERVICE + "=" + id, null, null, null,
				null);
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					Mobile mobile = new Mobile();
					mobile.setId_service(mCursor.getInt(0));
					mobile.setPlate(mCursor.getString(1));
					mobile.setMobile(mCursor.getString(2));
					mobile.setId_position(mCursor.getString(3));
					mobile.setStatus(mCursor.getInt(4));
					services.add(mobile);
				} while (mCursor.moveToNext());
			}
		}
		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return services;
	}

	public Vector<Mobile> readAll() {
		Vector<Mobile> services = new Vector<Mobile>();

		this.database.open();
		Cursor mCursor = this.database.getDataBase().query(
				SQLConstants.TABLE_NAME_MOBILE, SQLConstants.fieldsMobile,
				null, null, null, null, null);
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					Mobile mobile = new Mobile();
					mobile.setId_service(mCursor.getInt(0));
					mobile.setPlate(mCursor.getString(1));
					mobile.setMobile(mCursor.getString(2));
					mobile.setId_position(mCursor.getString(3));
					mobile.setStatus(mCursor.getInt(4));
					services.add(mobile);
				} while (mCursor.moveToNext());
			}
		}
		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return services;
	}

	public ArrayList<Mobile> readMobileByLastService() {
		ArrayList<Mobile> services = new ArrayList<Mobile>();

		this.database.open();
		Cursor mCursor = this.database.getDataBase().rawQuery(
				"SELECT * FROM mobile order by rowid asc limit 10", null);

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					Mobile mobile = new Mobile();
					mobile.setId_service(mCursor.getInt(0));
					mobile.setPlate(mCursor.getString(1));
					mobile.setMobile(mCursor.getString(2));
					mobile.setId_position(mCursor.getString(3));
					mobile.setStatus(mCursor.getInt(4));
					services.add(mobile);

				} while (mCursor.moveToNext());
			}
		}

		if (mCursor != null)
			mCursor.close();

		this.database.close();
		return services;
	}

	public void delete() {
	}

	public void delete(int id) {
		// TODO Auto-generated method stub

	}
}