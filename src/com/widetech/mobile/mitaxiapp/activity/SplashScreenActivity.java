package com.widetech.mobile.mitaxiapp.activity;

import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.activity.util.SystemUiHider;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.object.User;
import com.widetech.mobile.tools.GlobalConstants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashScreenActivity extends Activity {

	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mPreferences = getSharedPreferences(
				GlobalConstants.PREFERENCES_TAG, Context.MODE_PRIVATE);

		setContentView(R.layout.activity_splashscreen);
		Handler sp = new Handler();
		sp.postDelayed(new SplashHandler(), 2000);
	}

	protected void storeSharedPrefs(String value) {
		this.mEditor = mPreferences.edit();
		this.mEditor.putString(
				GlobalConstants.NAME_PREFERENCE_FIRST_OPENED_TAG, value);
		this.mEditor.commit();
	}

	private boolean first_time_check() {

		String first = this.mPreferences.getString(
				GlobalConstants.NAME_PREFERENCE_FIRST_OPENED_TAG, null);
		if ((first == null)) {
			return false;
		} else
			return true;
	}

	class SplashHandler implements Runnable {
		public void run() {

			try {
				User user = FacadeUser.read();

				// Only when app is opened for first.
				WidetechLogger.d("Estado de la preferencia: "
						+ first_time_check());

				if (!first_time_check()) {
					storeSharedPrefs("ok");
					Intent intentTutorial = new Intent(getApplicationContext(),
							HelpActivity.class);
					intentTutorial.putExtra("fromTutorial", true);
					startActivity(intentTutorial);
					finish();
					overridePendingTransition(
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadein,
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadeout);

				} else if (user == null) {
					Intent intentRegister = new Intent(getApplicationContext(),
							UserRegisterActivity.class);
					startActivity(intentRegister);
					finish();
					overridePendingTransition(
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadein,
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadeout);
				} else {
					Intent intentMain = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(intentMain);
					overridePendingTransition(
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadein,
							com.widetech.mobile.mitaxiapp.activity.R.anim.fadeout);
					finish();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}