package com.badrit.iqezny.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserdataDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "UserDBOpenHelper";

	private static final String DATABASE_NAME = "userdata.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_FRIENDS = "friends";
	public static final String COLUMN_FRIEND_ID = "friendID";
	public static final String COLUMN_FRIEND_NAME = "friendName";
	public static final String COLUMN_IS_SLEEP = "isSleep";
	public static final String COLUMN_IS_ZOMARA_USER = "isZomaraUser";

	public static final String TABLE_AWAKES = "awakes";
	public static final String COLUMN_AWAKE_ID = "awakeID";
	public static final String COLUMN_AWAKE_TIMESTAMP = "timestamp";

	public static final String TABLE_HELPS = "helps";
	public static final String COLUMN_HELP_ID = "helpID";
	public static final String COLUMN_HELP_FRIEND_ID = "friendID";
	public static final String COLUMN_HELP_TIMESTAMP = "timestamp";

	private static final String TABLE_FRIENDS_CREATE = "CREATE TABLE " + TABLE_FRIENDS + " (" + COLUMN_FRIEND_ID
			+ " VARCHAR PRIMARY KEY" + " , " + COLUMN_FRIEND_NAME + " VARCHAR(20) " + " , " + COLUMN_IS_SLEEP
			+ " BOOLEAN " + " , " + COLUMN_IS_ZOMARA_USER + " BOOLEAN" + ")";

	private static final String TABLE_AWAKES_CREATE = "CREATE TABLE " + TABLE_AWAKES + " (" + COLUMN_AWAKE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT " + " , " + COLUMN_AWAKE_TIMESTAMP + " TIMESTAMP " + ")";

	private static final String TABLE_HELPS_CREATE = "CREATE TABLE " + TABLE_HELPS + " (" + COLUMN_HELP_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT " + " , " + COLUMN_HELP_FRIEND_ID + " VARCHAR(20) " + " , "
			+ COLUMN_HELP_TIMESTAMP + " TIMESTAMP " + ")";

	public static UserdataDBOpenHelper getInstance(Context context) {
		if (userDBOpenHelper == null) {
			userDBOpenHelper = new UserdataDBOpenHelper(context);
		}
		return userDBOpenHelper;
	}

	private static UserdataDBOpenHelper userDBOpenHelper;

	private UserdataDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOGTAG, "Helper onCreate, creating the database");
		db.execSQL(TABLE_FRIENDS_CREATE);
		db.execSQL(TABLE_HELPS_CREATE);
		db.execSQL(TABLE_AWAKES_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AWAKES);
		onCreate(db);
		Log.i(LOGTAG, "Helper onUpgrade, upgrading the database");
	}
}
