package com.widetech.mobile.tools;

public class SQLConstants {
	
	//mobile constants
	public final static String TABLE_NAME_MOBILE = "mobile";
	public final static String MOBILE_ID_SERVICE = "id_service";
	public final static String MOBILE_NUMBER_PLATE = "number_plate";
	public final static String MOBILE_DESC = "description";
	public final static String MOBILE_ID_POSITION = "id_position";
	public final static String MOBILE_STATUS = "status";
	
	//user constants
	public final static String TABLE_NAME_USER = "user";
	public final static String USER_PHONE = "phone";
	public final static String USER_NAME = "name";
	public final static String USER_LASTNAME = "lastname";
	public final static String USER_EMAIL = "email";

	//address constants
	public final static String TABLE_NAME_ADDRESS = "address";
	public final static String ADDRESS_DESC = "description";
	public final static String ADDRESS_NEIGHBORHOOD = "neighborhood";
	public final static String ADDRESS_NOTE = "note";
	
	
	public final static String SQL_CREATE_TABLE_MOBILES = "CREATE TABLE "
			+ SQLConstants.TABLE_NAME_MOBILE + "("
			+ SQLConstants.MOBILE_ID_SERVICE+ " INTEGER NOT NULL, "
			+ SQLConstants.MOBILE_NUMBER_PLATE + " TEXT NOT NULL, "
			+ SQLConstants.MOBILE_DESC + " TEXT NOT NULL, " 
			+ SQLConstants.MOBILE_ID_POSITION + " TEXT NOT NULL, "
			+ SQLConstants.MOBILE_STATUS + " INTEGER NOT NULL )";
	
	
	public final static String SQL_CREATE_TABLE_USER = "CREATE TABLE "
			+ SQLConstants.TABLE_NAME_USER + "("
			+ SQLConstants.USER_PHONE + " TEXT NOT NULL, "
			+ SQLConstants.USER_NAME + " TEXT NOT NULL, "
			+ SQLConstants.USER_LASTNAME + " TEXT NOT NULL, "
			+ SQLConstants.USER_EMAIL + " TEXT NOT NULL )";
	
	
	public final static String SQL_CREATE_TABLE_ADDRESS = "CREATE TABLE "
			+ SQLConstants.TABLE_NAME_ADDRESS + "("
			+ SQLConstants.ADDRESS_DESC + " TEXT NOT NULL, "
			+ SQLConstants.ADDRESS_NEIGHBORHOOD + " TEXT NOT NULL, "
			+ SQLConstants.ADDRESS_NOTE + " TEXT NOT NULL )";
	
	public final static String SQL_QUERY_USER = "SELECT * FROM "
			+ SQLConstants.TABLE_NAME_USER + " LIMIT 1";
	
	
	public final static String fieldsMobile[] = new String[] {
		SQLConstants.MOBILE_ID_SERVICE,
		SQLConstants.MOBILE_NUMBER_PLATE,
		SQLConstants.MOBILE_DESC,
		SQLConstants.MOBILE_ID_POSITION,
		SQLConstants.MOBILE_STATUS};

	public final static String fieldsAddress[] = new String[] {
		SQLConstants.ADDRESS_DESC,
		SQLConstants.ADDRESS_NEIGHBORHOOD,
		SQLConstants.ADDRESS_NOTE};


}
