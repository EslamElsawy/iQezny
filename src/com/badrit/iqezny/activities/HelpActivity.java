package com.badrit.iqezny.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.badrit.iqezny.R;
import com.badrit.iqezny.DB.FriendsDataSource;
import com.badrit.iqezny.adapters.HelpTabsAdapter;
import com.badrit.iqezny.models.Friend;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HelpActivity extends ActionBarActivity implements ActionBar.TabListener {

	private static final String Help_Activity = "HelpActivity";
	private ViewPager viewPager;
	private ActionBar actionBar;
	private HelpTabsAdapter mAdapter;

	// Tab titles
	private String[] tabs = { "Sleeping", "Awake", "Statistics", "Settings" };

	@Override
	protected void onResume() {
		overridePendingTransition(0, 0);
		super.onResume();
	}

	@Override
	protected void onPause() {
		overridePendingTransition(0, 0);
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		Log.d("mytag", "Help Activity Created");

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.helppager);
		actionBar = getActionBar();
		mAdapter = new HelpTabsAdapter(getSupportFragmentManager(), this);

		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// on swiping the viewpager make respective tab selected
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// disable title of the Action bar
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	public void onLogoutButtonClicked(View v) {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startMainActivity();
	}

	private void startMainActivity() {
		Intent intent = new Intent(this, SignInActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	// Buttom Action Bar Methods
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.layout.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// int id = item.getItemId();
		//
		// if (id == R.id.Help) {
		// Toast toast = Toast.makeText(getApplicationContext(),
		// "This is the current Tab", Toast.LENGTH_LONG);
		// toast.show();
		// return true;
		//
		// } else if (id == R.id.Statistics) {
		// Intent intent = new Intent(this, StatisticsActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// startActivity(intent);
		// overridePendingTransition(0, 0);
		// return true;
		// } else if (id == R.id.Settings) {
		// Intent intent = new Intent(this, SettingsActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// startActivity(intent);
		// overridePendingTransition(0, 0);
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	// Tabs Methods
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(arg0.getPosition());

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	public void iamawakebutton(View v) {
		// send push notification to all friends

		// Get All Friends
		FriendsDataSource friendsDataSource = new FriendsDataSource(this);
		friendsDataSource.open();
		List<Friend> allFriends = friendsDataSource.finaAll(true);
		allFriends.addAll(friendsDataSource.finaAll(false));

		// Get my UserName and UserID
		ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
		String currUserID = (String) parseInstallation.get("facebookId");
		String currUserName = (String) parseInstallation.get("facebookName");

		// Send Push Notification to each One
		for (Friend friend : allFriends) {
			// Json Object
			JSONObject data = null;
			try {
				data = new JSONObject("{  \"action\": \"com.badit.iqezny.UPDATE_STATUS\", " + "\"type\": \"iamawake\","
						+ "\"fromId\": \"" + currUserID + "\"," + "\"fromUser\": \"" + currUserName + "\" }");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// Create our Installation query
			ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
			pushQuery.whereEqualTo("facebookId", friend.friendID);

			// Send push notification to query
			ParsePush push = new ParsePush();
			push.setQuery(pushQuery); // Set our Installation query
			push.setData(data);
			push.sendInBackground();

			// testing message
			Toast toast = Toast.makeText(this, "Sending I am awake to " + friend.friendName, Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
