package com.badrit.iqezny;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;

public class SignInActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
	}
}