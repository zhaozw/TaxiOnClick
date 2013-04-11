package com.widetech.mobile.tools;

/*
 * WidetechTools.java
 *
 * Created on December 3, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.widetech.mobile.mitaxiapp.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/*Utilerias de la Aplicacion*/

public class WideTechTools {

	/* Retorna la fecha Actual del Celular en formato 'yyyy-MM-dd' */
	public static String getDatePhone() {
		Calendar cal = new GregorianCalendar();
		Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formatteDate = df.format(date);
		return formatteDate;
	}

	/* Retorna la fecha Actual del celular en formato 'HH:mm:ss' */
	public static String getHourPhone() {
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String formatteHour = df.format(dt.getTime());
		return formatteHour;
	}

	/*
	 * Retorna el IMEI del Dispositivo. Recibe como parametro el contexto de la
	 * actividad desde la cual es invocada
	 */
	public static String getImeiPhone(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return phoneManager.getDeviceId();
		// return "351554056021745";
	}

	/*
	 * Retorna el Carrier que Utiliza el dispositivo. Recibe como parametro el
	 * contexto de al actividad desde la cual es invocada
	 */
	public static String getCarrierPhone(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneManager.getNetworkOperatorName();
	}

	/* Retorna el marca del Dispositivo */
	public static String getMarkPhone() {
		return android.os.Build.MANUFACTURER;
	}

	/* Retorna el modelo del Dispositivo */
	public static String getModelPhone() {
		return android.os.Build.MODEL;
	}

	/* Retorna la version del sistema operativo del dispositivo */
	public static String getVersionOs() {

		return String.valueOf(android.os.Build.VERSION.RELEASE);
	}

	/* Retorna el nombre de la version del sistema operativo del dispositivo */
	public static String getNameOs() {
		StringBuilder builder = new StringBuilder();
		builder.append(GlobalConstants.NAME_PLATTFORM_ANDROID + " ");

		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			int fieldValue = -1;

			try {
				fieldValue = field.getInt(new Object());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			if (fieldValue == Build.VERSION.SDK_INT) {
				builder.append(fieldName);
			}
		}
		String tmp = builder.toString().replace("_", " ");
		tmp = tmp.toLowerCase();
		tmp = WideTechTools.capitalizeLetters(tmp);
		return tmp;
	}

	/* Retorna el numero de version de la aplicacion */
	public static String getVersionApp(Context c) {

		String version = "";

		try {
			version = c.getPackageManager().getPackageInfo(c.getPackageName(),
					0).versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	/* A partir de una frase dada convierte las primeras letras en mayusculas */
	public static String capitalizeLetters(String phrase) {

		boolean prevWasWhiteSp = true;
		char[] chars = phrase.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				if (prevWasWhiteSp) {
					chars[i] = Character.toUpperCase(chars[i]);
				}
				prevWasWhiteSp = false;
			} else {
				prevWasWhiteSp = Character.isWhitespace(chars[i]);
			}
		}
		return new String(chars);
	}

	// Return the level of Battery remaining
	public static int getLevelBattery(Context c) {
		int level = -1;
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = c.registerReceiver(null, filter);
		int rawlevel = batteryStatus
				.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		if (rawlevel >= 0 && scale > 0) {
			level = (rawlevel * 100) / scale;
		}
		return level;
	}

	// Obtain network type from device
	public static String getNetworkType(Context context) {
		String type = "N/A";

		NetworkInfo active_network = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (active_network != null && active_network.isConnectedOrConnecting()) {
			if (active_network.getType() == ConnectivityManager.TYPE_WIFI) {
				type = "WIFI";
			} else if (active_network.getType() == ConnectivityManager.TYPE_MOBILE) {
				type = ((ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE))
						.getActiveNetworkInfo().getSubtypeName();
			}
		}

		return type;
	}

	// Display messages with Fancy Style
	public static void displayMessage(String message, Context context,
			Activity activity) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout_message,
				(ViewGroup) activity.findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
}