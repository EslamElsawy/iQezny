package com.badrit.iqezny.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badrit.iqezny.R;

public class StatisticsFragment extends Fragment {

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

		refreshStatistics();

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		refreshStatistics();
	}
	
	private void refreshStatistics() {
		SharedPreferences settings = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

		// Awakes
		int awakes = settings.getInt("awakes", 0);
		int days = settings.getInt("days", 10);

		TextView textAwakes = (TextView) rootView.findViewById(R.id.textawakes);
		TextView textAwakesValue = (TextView) rootView.findViewById(R.id.textawakesvalue);
		textAwakes.setText(awakes + " Prayed Fajr");
		double awakesAvg = days == 0 ? 100 : (awakes / (days * 1.0)) * 100;
		textAwakesValue.setText(awakesAvg + "%");

		// Missed
		int missed = days - awakes;

		TextView textMissed = (TextView) rootView.findViewById(R.id.textmissed);
		TextView textMissedValue = (TextView) rootView.findViewById(R.id.textmissedvalue);
		textMissed.setText(missed + " Missed Fajr");
		double missedAvg = days == 0 ? 100 : (missed / (days * 1.0)) * 100;
		textMissedValue.setText(missedAvg + "%");

		// Alarms
		int alarms = settings.getInt("alarms", 0);
		TextView textalarms = (TextView) rootView.findViewById(R.id.textalarms);
		TextView textalarmsvalue = (TextView) rootView.findViewById(R.id.textalarmsvalue);
		textalarms.setText(alarms + " Alarms Sent");
		double alarmsAvg = days == 0 ? 0 : alarms / (days * 1.0);
		textalarmsvalue.setText("avg: " + alarmsAvg + " per day");

		// Voices
		int voices = settings.getInt("voices", 0);
		TextView textvoices = (TextView) rootView.findViewById(R.id.textvoices);
		TextView textvoicesvalue = (TextView) rootView.findViewById(R.id.textvoicesvalue);
		textvoices.setText(voices + " Voices Sent");
		double voicesAvg = days == 0 ? 0 : voices / (days * 1.0);
		textvoicesvalue.setText("avg: " + voicesAvg + " per day");

	}

}
