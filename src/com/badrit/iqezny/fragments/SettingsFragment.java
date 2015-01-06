package com.badrit.iqezny.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.badrit.iqezny.R;
import com.badrit.iqezny.activities.CountriesActivity;
import com.badrit.iqezny.models.City;
import com.badrit.iqezny.prayertimes.PrayTime;

public class SettingsFragment extends Fragment {

	ListView listView;
	View rootView;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshSettingsTextView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_settings, container, false);

		refreshSettingsTextView();

		listView = (ListView) rootView.findViewById(R.id.settingslist);
		String[] values = new String[] { "Location" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.settinglistitem,
				R.id.settingstextview, values);

		// Assign adapter to ListView
		listView.setAdapter(adapter);

		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// ListView Clicked item value
				String itemValue = (String) listView.getItemAtPosition(position);

				if (itemValue.equals("Location")) {
					Intent intent = new Intent(getActivity(), CountriesActivity.class);
					startActivity(intent);
				}
			}
		});

		return rootView;
	}

	private void refreshSettingsTextView() {
		// Genearting Setting values
		SharedPreferences settings = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
		String cityString = settings.getString("locationInfo", null);
		String[] split = cityString.split("\t+");
		City city = new City(split[0], split[1], split[2], split[3], split[4], split[5]);

		// getting the prayer time
		PrayTime prayTime = new PrayTime();
		prayTime.setTimeFormat(1);
		prayTime.setCalcMethod(5);
		Float timeZone = Float.parseFloat(city.cityTimeZone);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		month += 1;
		int year = calendar.get(Calendar.YEAR);
		String currDate = day + " " + month + " " + year;

		ArrayList<String> times = prayTime.getDatePrayerTimes(year, month, day, Double.parseDouble(city.cityLatitude),
				Double.parseDouble(city.cityLongitude), timeZone.intValue());

		String fajrTime = times.get(0);
		String sheroqTime = times.get(1);

		// showing results
		TextView textView = (TextView) rootView.findViewById(R.id.textviewsettings);
		textView.setText("City: " + city.cityName + "\n" + "Country: " + city.cityCountry + "\n" + "TimeZone: "
				+ timeZone.intValue() + "\n" + "Latitude: " + city.cityLatitude + "\n" + "Longituded: "
				+ city.cityLongitude + "\n" + "SystemClock: " + new Date(System.currentTimeMillis()) + "\n" + "Date: "
				+ currDate + "\n" + "Fajr: " + fajrTime + "\n" + "Sherouq: " + sheroqTime);
	}
}
