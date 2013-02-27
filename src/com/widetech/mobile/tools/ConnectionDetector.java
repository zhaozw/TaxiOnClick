/*
 * ConnectionDetector.java
 *
 * Created on December 3, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Permite detectar el estado de las diversas interfaces
 * de conexion a internet, en caso de encontrar alguna activa
 * permite realizar peticiones al servidor
 * **/
public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}