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

import com.badrit.iqezny.models.City;

public class CitiesDataSource {

	private static final String LOGTAG = "CitiesDataSource";

	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	Context context;

	private static final String LIMIT = "100";

	private static final String[] allColums = { LocationDBOpenHelper.COLUMN_CITY_ID,
			LocationDBOpenHelper.COLUMN_CITY_NAME, LocationDBOpenHelper.COLUMN_CITY_COUNTRY,
			LocationDBOpenHelper.COLUMN_CITY_TIMEZONE, LocationDBOpenHelper.COLUMN_CITY_LATITUDE,
			LocationDBOpenHelper.COLUMN_CITY_LONGITUDE };

	public CitiesDataSource(Context context) {
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

	public City create(City city) {

		ContentValues values = new ContentValues();
		values.put(LocationDBOpenHelper.COLUMN_CITY_ID, city.cityId);
		values.put(LocationDBOpenHelper.COLUMN_CITY_NAME, city.cityName);
		values.put(LocationDBOpenHelper.COLUMN_CITY_COUNTRY, city.cityCountry);
		values.put(LocationDBOpenHelper.COLUMN_CITY_TIMEZONE, city.cityTimeZone);
		values.put(LocationDBOpenHelper.COLUMN_CITY_LATITUDE, city.cityLatitude);
		values.put(LocationDBOpenHelper.COLUMN_CITY_LONGITUDE, city.cityLongitude);

		db.insert(LocationDBOpenHelper.TABLE_CITIES, null, values);
		return city;
	}

	public List<City> finaAll(String countryName) {
		List<City> cities = new ArrayList<City>();

		Cursor cursor = db
				.query(LocationDBOpenHelper.TABLE_CITIES, allColums, LocationDBOpenHelper.COLUMN_CITY_COUNTRY + "=?",
						new String[] { countryName }, null, null, LocationDBOpenHelper.COLUMN_CITY_NAME + " ASC", LIMIT);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String cityId = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_ID));
				String cityName = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_NAME));
				String cityCountry = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_COUNTRY));
				String cityTimeZone = cursor
						.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_TIMEZONE));
				String cityLatitude = cursor
						.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_LATITUDE));
				String cityLongitude = cursor.getString(cursor
						.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_LONGITUDE));
				City city = new City(cityId, cityName, cityCountry, cityTimeZone, cityLatitude, cityLongitude);
				cities.add(city);
			}
		}
		return cities;
	}

	public List<City> searchByInputText(String inputText, String countryName) throws SQLException {

		List<City> cities = new ArrayList<City>();
		String query = "SELECT " + LocationDBOpenHelper.COLUMN_CITY_ID + " as _id" + ","
				+ LocationDBOpenHelper.COLUMN_CITY_NAME + "," + LocationDBOpenHelper.COLUMN_CITY_COUNTRY + ","
				+ LocationDBOpenHelper.COLUMN_CITY_TIMEZONE + "," + LocationDBOpenHelper.COLUMN_CITY_LATITUDE + ","
				+ LocationDBOpenHelper.COLUMN_CITY_LONGITUDE + " from " + LocationDBOpenHelper.TABLE_CITIES + " where "
				+ LocationDBOpenHelper.COLUMN_CITY_NAME + " LIKE '" + inputText + "%' AND "
				+ LocationDBOpenHelper.COLUMN_CITY_COUNTRY + "='" + countryName + "' " + " ORDER BY "
				+ LocationDBOpenHelper.COLUMN_CITY_NAME + " ASC" + " LIMIT " + LIMIT + " ;";

		Log.i(LOGTAG, "Query " + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String cityId = cursor.getString(cursor.getColumnIndex("_id"));
				String cityName = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_NAME));
				String cityCountry = cursor.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_COUNTRY));
				String cityTimeZone = cursor
						.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_TIMEZONE));
				String cityLatitude = cursor
						.getString(cursor.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_LATITUDE));
				String cityLongitude = cursor.getString(cursor
						.getColumnIndex(LocationDBOpenHelper.COLUMN_CITY_LONGITUDE));

				City city = new City(cityId, cityName, cityCountry, cityTimeZone, cityLatitude, cityLongitude);
				cities.add(city);
			} while (cursor.moveToNext());
		}

		Log.i(LOGTAG, "Row match " + cursor.getCount());
		return cities;

	}
}
