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

	// Taga para el loger de widetech
	public static final String TAG_APP = "mitaxi";

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

}