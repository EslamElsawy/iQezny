package com.badrit.iqezny.adapters;

import com.badrit.iqezny.fragments.StatisticsAwakesFragment;
import com.badrit.iqezny.fragments.StatisticsHelpsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StatisticsTabsAdapter extends FragmentPagerAdapter {

	public StatisticsTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new StatisticsAwakesFragment();
		case 1:
			return new StatisticsHelpsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
