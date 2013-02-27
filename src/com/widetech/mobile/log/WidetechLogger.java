/*
 * DetailLogger.java
 *
 * Created on December 11, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.log;

import android.util.Log;
import com.widetech.mobile.tools.GlobalConstants;

public class WidetechLogger {

	public static void d(String message) {
		if (GlobalConstants.LOG_DEBUG_ENABLED)
			Log.d(GlobalConstants.TAG_APP, message);
	}

	public static void i(String message) {
		Log.i(GlobalConstants.TAG_APP, message);
	}

	public static void e(final String message, Throwable e) {
		if (e != null)
			Log.e(GlobalConstants.TAG_APP, message, e);
		else
			Log.e(GlobalConstants.TAG_APP, message);
	}
}