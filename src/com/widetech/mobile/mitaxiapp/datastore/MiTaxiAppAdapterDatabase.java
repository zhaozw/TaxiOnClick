/*
 * EyeRemoteAdapterDatabase.java
 *
 * Created on December 11, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.mitaxiapp.datastore;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MiTaxiAppAdapterDatabase {

	private Context context;
	private SQLiteDatabase database;
	private MiTaxiAppSQLiteHelper connector;

	public MiTaxiAppAdapterDatabase(Context context) {
		this.context = context;
	}

	public MiTaxiAppAdapterDatabase open() throws SQLException {

		this.connector = new MiTaxiAppSQLiteHelper(this.context);
		this.database = this.connector.getWritableDatabase();
		return this;
	}

	public void close() {

		this.connector.close();
	}

	public SQLiteDatabase getDataBase() {

		return this.database;
	}
}
