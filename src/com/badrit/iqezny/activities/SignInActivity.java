package com.badrit.iqezny.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.badrit.iqezny.R;
import com.badrit.iqezny.DB.FriendsDataSource;
import com.badrit.iqezny.models.Friend;
import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SignInActivity extends Activity {

	private static final String Sign_In_Activity = "SignInActivity";
	FriendsDataSource friendsDataSource;
	SharedPreferences settings;

	private enum NextActivity {
		CountriesActivity, HelpActivity
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ParseAnalytics
		ParseAnalytics.trackAppOpenedInBackground(getIntent());

		// Check if previously logged In
		settings = getSharedPreferences("pref", MODE_PRIVATE);
		boolean previouslyLoggedIn = settings.getBoolean("loggedIn", false);
		if (previouslyLoggedIn) {
			setContentView(R.layout.activity_landingpage);
			Log.d(Sign_In_Activity, "User Previously Logged In");
			logIn(NextActivity.HelpActivity);
		} else {
			setContentView(R.layout.activity_sign_in);
		}
	}

	public void onLogInButtonClicked(View v) {
		logIn(NextActivity.CountriesActivity);
	}

	private void logIn(final NextActivity nextActivity) {
		List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d(Sign_In_Activity, err.getMessage());
					Log.d(Sign_In_Activity, "The user cancelled the Facebook login.");
				} else {
					// Save the information into preferences
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("loggedIn", true);
					editor.commit();

					if (user.isNew()) {
						Log.d(Sign_In_Activity, "New User Logged In: " + user.getUsername());
						makeMeRequest();
						makeFriendsRequest(nextActivity);
					} else {
						Log.d(Sign_In_Activity, "Old User Logged In: " + user.getUsername());
						makeMeRequest();
						makeFriendsRequest(nextActivity);
					}
				}

			}
		});
	}

	private void makeFriendsRequest(final NextActivity nextActivity) {

		// Open DB connection
		friendsDataSource = new FriendsDataSource(this);
		friendsDataSource.open();

		Log.d(Sign_In_Activity, "make frineds request method is called");
		Log.d(Sign_In_Activity, ParseFacebookUtils.getSession().getAccessToken());
		Log.d(Sign_In_Activity, ParseFacebookUtils.getSession().getApplicationId());
		Log.d(Sign_In_Activity, ParseFacebookUtils.getSession().getPermissions().toString());
		Log.d(Sign_In_Activity, ParseFacebookUtils.getSession().getState().name());

		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users, Response response) {
						Log.d(Sign_In_Activity, "make friends request callback is called");
						Log.d(Sign_In_Activity, "Returned users list size: " + users.size());

						// clear database
						friendsDataSource.clearAllData();

						// Store friends to DB
						boolean isSleep = true;
						boolean isZomaraUser = true;
						for (int i = 0; i < users.size(); i++) {
							GraphUser currUser = users.get(i);
							Friend friend = new Friend(currUser.getId(), currUser.getName(), isSleep, isZomaraUser);
							friendsDataSource.create(friend);
						}

						// start the next Activity
						if (nextActivity.equals(NextActivity.HelpActivity))
							startHelpActivity();
						else if (nextActivity.equals(NextActivity.CountriesActivity))
							startCountriesActivity();
					}
				});

		// excute request
		request.executeAsync();
	}

	private void makeMeRequest() {
		// create request
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					Log.d(Sign_In_Activity, "Start adding fields to ParseUser");
					ParseUser currentUser = ParseUser.getCurrentUser();
					currentUser.put("facebookId", user.getId());
					currentUser.put("facebookName", user.getName());
					currentUser.put("facebookUsername", user.getUsername() + "");
					currentUser.put("facebookLink", user.getLink());
					currentUser.saveInBackground();
					Log.d(Sign_In_Activity, "end adding fields to ParseUser");

					// Subscribe for push notification
					subscribe(user);

					// Notify my friends

				}
			}

			private void subscribe(GraphUser user) {

				Log.d(Sign_In_Activity, "Start adding fields to parseInstallation");
				ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();

				parseInstallation.put("facebookId", user.getId());
				parseInstallation.put("facebookName", user.getName());
				parseInstallation.put("facebookUsername", user.getUsername() + "");
				// PushService.subscribe(getApplicationContext(), "user" +
				// user.getId(), HelpActivity.class);

				ParsePush.subscribeInBackground("user" + user.getId(), new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							Log.d(Sign_In_Activity, "successfully subscribed to the broadcast channel.");
						} else {
							Log.e(Sign_In_Activity, "failed to subscribe for push", e);
						}
					}
				});

				parseInstallation.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Log.d(Sign_In_Activity, "succesfully saved parseInstallation");
						} else {
							Log.d(Sign_In_Activity, "failed to save parseInstallation");
							e.printStackTrace();
						}
					}
				});
				Log.d(Sign_In_Activity, "end adding fields to ParseUser");

			}
		});

		// excute request
		request.executeAsync();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	private void startHelpActivity() {
		Intent intent = new Intent(this, HelpActivity.class);
		finish();
		startActivity(intent);
	}

	private void startCountriesActivity() {
		Intent intent = new Intent(this, CountriesActivity.class);
		finish();
		startActivity(intent);
	}

}
