package com.widetech.mobile.mitaxiapp.activity;

import com.widetech.mobile.mitaxiapp.activity.R;
import com.widetech.mobile.mitaxiapp.activity.util.SystemUiHider;
import com.widetech.mobile.mitaxiapp.facade.FacadeUser;
import com.widetech.mobile.mitaxiapp.object.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splashscreen);
		Handler sp = new Handler();
		sp.postDelayed(new SplashHandler(), 2000);
	}

	class SplashHandler implements Runnable {
		public void run() {

			try {
				User user = FacadeUser.read();

				if (user == null) {
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
