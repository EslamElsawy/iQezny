package com.badrit.iqezny.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.badrit.iqezny.models.Country;

public class CountriesDataSource {

	private static final String LOGTAG = "CountriesDataSource";

	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	Context context;

	private static final String[] allColums = { LocationDBOpenHelper.COLUMN_COUNTRY_CODE,
			LocationDBOpenHelper.COLUMN_COUNTRY_NAME };

	public CountriesDataSource(Context context) {
		this.context = context;
		dbHelper = LocationDBOpenHelper.getInstance(context);
	}

	public void open() {
		Log.i(LOGTAG, "DataBase opened");
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "DataBase closed");
		dbHelper.close();
	}

	public Country create(Country country) {

		ContentValues values = new ContentValues();
		values.put(LocationDBOpenHelper.COLUMN_COUNTRY_CODE, country.countryCode);
		values.put(LocationDBOpenHelper.COLUMN_COUNTRY_NAME, country.countryName);

		db.insert(LocationDBOpenHelper.TABLE_COUNTRIES, null, values);
		return country;
	}

	public List<Country> finaAll() {
		List<Country> countries = new ArrayList<Country>();

		Cursor cursor = db.query(LocationDBOpenHelper.TABLE_COUNTRIES, allColums, null, null, null, null,
				LocationDBOpenHelper.COLUMN_COUNTRY_NAME + " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String countryCode = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_COUNTRY_CODE));
				String countryName = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_COUNTRY_NAME));
				Country country = new Country(countryCode, countryName);
				countries.add(country);
			}
		}
		return countries;
	}

	private boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

	public List<Country> searchByInputText(String inputText) throws SQLException {

		List<Country> countries = new ArrayList<Country>();
		String query = "SELECT " + LocationDBOpenHelper.COLUMN_COUNTRY_CODE + " as _id" + ","
				+ LocationDBOpenHelper.COLUMN_COUNTRY_NAME + " from " + LocationDBOpenHelper.TABLE_COUNTRIES
				+ " where " + LocationDBOpenHelper.COLUMN_COUNTRY_NAME + " LIKE '" + inputText + "%' " + " ORDER BY "
				+ LocationDBOpenHelper.COLUMN_COUNTRY_NAME + " ASC" + " ;";

		Log.i(LOGTAG, "Query " + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String countryCode = cursor.getString(cursor.getColumnIndex("_id"));
				String countryName = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_COUNTRY_NAME));
				Log.i(LOGTAG, "Query " + countryCode);
				Country country = new Country(countryCode, countryName);
				countries.add(country);
			} while (cursor.moveToNext());
		}

		Log.i(LOGTAG, "Row match " + cursor.getCount());
		return countries;

	}
}
