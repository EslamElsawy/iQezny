package com.badrit.iqezny;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize Crash Reporting.
		ParseCrashReporting.enable(this);

		// Add your initialization code here
		Parse.initialize(this, "25cvHnYyyb7q0wkcYff2YygRCRiif1Qy2EWv23iU",
				"8E1cgLCQDbFF7HcywpqQDL8xktl5BUkC864kZaqK");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

		// Testing parse
		ParseUser user = new ParseUser();
		user.setUsername("my name5");
		user.setPassword("my pass5");
		user.setEmail("eslam55@example.com");

		// other fields can be set just like with ParseObject
		user.put("phone", "650-555-0000");

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					// Hooray! Let them use the app now.
					System.out.println("Done signing up");
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
					System.err.println("can't sign up");
				}
			}
		});
	}
}
