package com.badrit.iqezny.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.badrit.iqezny.R;
import com.badrit.iqezny.DB.FriendsDataSource;
import com.badrit.iqezny.adapters.ImageAdapter;
import com.badrit.iqezny.models.BaseListElement;
import com.badrit.iqezny.models.Friend;
import com.badrit.iqezny.models.PeopleListElement;

public class HelpSleepingFragment extends Fragment {

	private static HelpSleepingFragment instance = null;
	private List<BaseListElement> sleepingFriends;
	private ImageAdapter imageAdapter;
	private Context context;

	public static HelpSleepingFragment getInstance() {
		return instance;
	}

	public HelpSleepingFragment(Context context) {
		super();
		sleepingFriends = getFriends(true);
		instance = this;
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_help_sleeping, container, false);
		GridView gridview = (GridView) rootView.findViewById(R.id.gridviewhelpsleeping);
		imageAdapter = new ImageAdapter(context, sleepingFriends);
		gridview.setAdapter(imageAdapter);

		return rootView;
	}

	public void refresh() {
		sleepingFriends.clear();
		sleepingFriends.addAll(getFriends(true));
		Log.d("mytag", "new sleeping friends:" + sleepingFriends.size());
		imageAdapter.notifyDataSetChanged();
	}

	private List<BaseListElement> getFriends(boolean isSleep) {

		// Open DB connection
		FriendsDataSource friendsDataSource = new FriendsDataSource(context);
		friendsDataSource.open();

		List<BaseListElement> listElements = new ArrayList<BaseListElement>();

		List<Friend> sleepingFriends = friendsDataSource.finaAll(isSleep);

		int requestCode = 0;
		for (Friend friend : sleepingFriends) {
			String currId = friend.friendID;
			String currName = friend.friendName;
			PeopleListElement element = new PeopleListElement(context, currId, currName, currId + "", requestCode++);
			listElements.add(element);
		}

		return listElements;
	}
}
