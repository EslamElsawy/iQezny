package com.badrit.iqezny.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.badrit.iqezny.models.Friend;

public class FriendsDataSource {
	private static final String LOGTAG = "FriendsDataSource";

	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	Context context;

	private static final String[] allColums = { UserdataDBOpenHelper.COLUMN_FRIEND_ID,
			UserdataDBOpenHelper.COLUMN_FRIEND_NAME, UserdataDBOpenHelper.COLUMN_IS_SLEEP,
			UserdataDBOpenHelper.COLUMN_IS_ZOMARA_USER };

	public FriendsDataSource(Context context) {
		Log.d(LOGTAG, "The context is: "+context);
		this.context = context;
		dbHelper = UserdataDBOpenHelper.getInstance(context);
	}

	public void open() {
		Log.i(LOGTAG, "DataBase userdata opened");
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "DataBase userdata closed");
		dbHelper.close();
	}

	public Friend create(Friend friend) {

		ContentValues values = new ContentValues();
		values.put(UserdataDBOpenHelper.COLUMN_FRIEND_ID, friend.friendID);
		values.put(UserdataDBOpenHelper.COLUMN_FRIEND_NAME, friend.friendName);
		values.put(UserdataDBOpenHelper.COLUMN_IS_SLEEP, friend.isSleep);
		values.put(UserdataDBOpenHelper.COLUMN_IS_ZOMARA_USER, friend.isZomaraUser);

		db.insert(UserdataDBOpenHelper.TABLE_FRIENDS, null, values);
		return friend;
	}

	public List<Friend> finaAll(boolean isSleep) {
		List<Friend> friends = new ArrayList<Friend>();

		String isSleepString = isSleep ? "1" : "0";
		Cursor cursor = db.query(UserdataDBOpenHelper.TABLE_FRIENDS, allColums, UserdataDBOpenHelper.COLUMN_IS_SLEEP
				+ "=?", new String[] { isSleepString }, null, null, null, null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String friendID = cursor.getString(cursor.getColumnIndex(UserdataDBOpenHelper.COLUMN_FRIEND_ID));
				String friendName = cursor.getString(cursor.getColumnIndex(UserdataDBOpenHelper.COLUMN_FRIEND_NAME));
				boolean friendIsSleep = cursor.getInt(cursor.getColumnIndex(UserdataDBOpenHelper.COLUMN_IS_SLEEP)) > 0;
				boolean friendIsZomaraUser = cursor.getInt(cursor
						.getColumnIndex(UserdataDBOpenHelper.COLUMN_IS_ZOMARA_USER)) > 0;
				Friend friend = new Friend(friendID, friendName, friendIsSleep, friendIsZomaraUser);
				friends.add(friend);
			}
		}
		return friends;
	}

	public ArrayList<String> finaAllIDs() {
		ArrayList<String> ids = new ArrayList<String>();

		Cursor cursor = db.query(UserdataDBOpenHelper.TABLE_FRIENDS, allColums, null, null, null, null, null, null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String friendID = cursor.getString(cursor.getColumnIndex(UserdataDBOpenHelper.COLUMN_FRIEND_ID));
				ids.add(friendID);
			}
		}
		return ids;
	}

	public void setUserAsAwake(String fromId) {
		Log.i(LOGTAG, "Updating User status ");

		ContentValues cv = new ContentValues();
		cv.put(UserdataDBOpenHelper.COLUMN_IS_SLEEP, "0");
		db.update(UserdataDBOpenHelper.TABLE_FRIENDS, cv, UserdataDBOpenHelper.COLUMN_FRIEND_ID + " = " + fromId, null);
	}

	public void clearAllData() {
		db.delete(UserdataDBOpenHelper.TABLE_FRIENDS, null, null);
	}
}
