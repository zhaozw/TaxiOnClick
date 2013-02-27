/*
 * EyeRemoteSQLiteHelper.java
 *
 * Created on December 7, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.mitaxiapp.datastore;

import com.widetech.mobile.tools.GlobalConstants;
import com.widetech.mobile.tools.SQLConstants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase para la creacion y actualizacion de la base de datos en Android
 * */

public class MiTaxiAppSQLiteHelper extends SQLiteOpenHelper {

	public MiTaxiAppSQLiteHelper(Context context) {
		super(context, GlobalConstants.NAME_DATABASE, null,
				GlobalConstants.NUMBER_VERSION_DATABASE);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQLConstants.SQL_CREATE_TABLE_USER);
		db.execSQL(SQLConstants.SQL_CREATE_TABLE_MOBILES);
		db.execSQL(SQLConstants.SQL_CREATE_TABLE_ADDRESS);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
