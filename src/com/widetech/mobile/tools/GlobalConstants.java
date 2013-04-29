/*
 * GlobalConstans.java
 *
 * Created on December 7, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

/*Creacion de constantes para la aplicacion*/
package com.widetech.mobile.tools;

public class GlobalConstants {

	// Status del log de widetech
	public static final boolean LOG_DEBUG_ENABLED = true;

	// Tag para el loger de widetech
	public static final String TAG_APP = "mitaxi";

	// Tag para las preferencias de mitaxi
	public static final String PREFERENCES_TAG = "mitaxipreferences";

	// Nombre de la preferencia de verificacion cuando se abre la app por
	// primera vez
	public static final String NAME_PREFERENCE_FIRST_OPENED_TAG = "first";

	// Nombre de la preferencia de el ultimo id del servicio solicitado
	public static final String NAME_PREFERENCE_LAST_ID_SERVICE = "lastid";
	
	// El nombre como prefijo de plataforma
	public static final String NAME_PLATTFORM_ANDROID = "Android";

	// El nombre de la base de datos de detalle de EyeControl
	public static final String NAME_DATABASE = "GetTaxiDB";

	// El numero de la version de la base de datos de detalle de EyeControl
	public static final int NUMBER_VERSION_DATABASE = 1;

	// User for validate web service
	public static final String USER_SERVICE = "widetech";

	// Password for validate web service
	public static final String PASS_SERVICE = "widetech";

	// User for validate web service actual location taxi
	public static final String USER_TAXI = "taxi";

	// Password for validate web service location taxi
	public static final String PASS_TAXI = "taxi";

	// Address wildcard
	public static final String ADDRESS_WILDCARD = "Calle 70 # 9-87";

	// Sector wildcard
	public static final String SECTOR_WILDCARD = "POR ASIGNAR";

	// Note wildcard
	public static final String NOTE_WILDCARD = "CASA";

	// tag del metodo POST del request
	public static final String METHOD_POST = "POST";

	/* Constantes para realizar el parsing del xml de el server */
	public static final String RESULT = "result";

	// Request exitoso en el registro del usuario
	public static final String SUCCESS_REGISTER = "OK";

	// Request exitoso en la cancelación del taxi
	public static final String SUCCESS_CANCEL = "OK";

	public static final String CITY_APP = "Bogotá";

	public static final String URL_GOOGLE_API_MAPS = "http://maps.googleapis.com/maps/api/geocode/json";

	/* Status de los taxis (En camino, finalizado, cancelado) */
	public static final int TAXI_IN_COURSE = 0;
	public static final int TAXI_ARRIVED = 1;
	public static final int TAXI_CANCELED = 2;

}
