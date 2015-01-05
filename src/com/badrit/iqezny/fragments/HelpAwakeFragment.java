package com.badrit.iqezny.fragments;

import java.util.ArrayList;
import java.util.List;

import com.badrit.iqezny.R;
import com.badrit.iqezny.DB.FriendsDataSource;
import com.badrit.iqezny.adapters.ImageAdapter;
import com.badrit.iqezny.models.BaseListElement;
import com.badrit.iqezny.models.Friend;
import com.badrit.iqezny.models.PeopleListElement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class HelpAwakeFragment extends Fragment {

	private static HelpAwakeFragment instance = null;
	private ImageAdapter imageAdapter;
	private List<BaseListElement> awakeFriends;
	private Context context;

	public static HelpAwakeFragment getInstance() {
		return instance;
	}

	public HelpAwakeFragment(Context context) {
		super();
		instance = this;
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//Data
		awakeFriends = getFriends(false);
		
		//View
		View rootView = inflater.inflate(R.layout.fragment_help_awake, container, false);
		GridView gridview = (GridView) rootView.findViewById(R.id.gridviewhelpawake);
		imageAdapter = new ImageAdapter(context, awakeFriends);
		gridview.setAdapter(imageAdapter);

		return rootView;
	}

	public void refresh() {
		awakeFriends.clear();
		awakeFriends.addAll(getFriends(false));
		Log.d("mytag", "new awake friends:" + awakeFriends.size());
		imageAdapter.notifyDataSetChanged();
	}

	public List<BaseListElement> getFriends(boolean isSleep) {

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
