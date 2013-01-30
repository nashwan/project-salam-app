package com.example.salambuney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by: Yoosuf nabeel solih / 20 December 2012
 * 
 */
public class SalaamDB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "salaam_db";
	private static final int DATABASE_VERSION = 2;

	// message template table related
	public static final String TEMPLATES_TABLE_NAME = "templates";
	public static final String TEMPLATE_MESSAGE = "message";
	public static final String TEMPLATE_ID = "_id";

	// history table related
	public static final String HISTORY_TABLE_NAME = "history";
	public static final String HISTORY_MESSAGE = "message";
	public static final String HISTORY_ID = "_id";
	public static final String HISTORY_SENT_TIME = "sent_time";
	public static final String HISTORY_CONATCT = "contact";

	// contact table related
	public static final String CONTACT_TABLE_NAME = "contact";
	public static final String CONTACT_PHONE = "phone";
	public static final String CONTACT_ID = "_id";
	public static final String CONTACT_NAME = "name";

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE " + HISTORY_TABLE_NAME + " (" + HISTORY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + HISTORY_MESSAGE
				+ " TEXT NOT NULL, " + HISTORY_SENT_TIME + " TEXT NOT NULL, "
				+ HISTORY_CONATCT + " TEXT)";

		db.execSQL(sql);

		sql = "CREATE TABLE " + TEMPLATES_TABLE_NAME + " (" + TEMPLATE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPLATE_MESSAGE
				+ " TEXT NOT NULL) ";

		db.execSQL(sql);

		sql = "CREATE TABLE " + CONTACT_TABLE_NAME + " (" + CONTACT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACT_NAME
				+ " TEXT NOT NULL, " + CONTACT_PHONE + " TEXT NOT NULL)";

		db.execSQL(sql);

		insertInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists " + TEMPLATES_TABLE_NAME);
		db.execSQL("drop table if exists " + HISTORY_TABLE_NAME);
		db.execSQL("drop table if exists " + CONTACT_TABLE_NAME);
		onCreate(db);
	}

	private void insertInitialData(SQLiteDatabase db) {

		// insert intial templates
		db.execSQL("INSERT INTO templates VALUES(1,'I am not feeling well today.')");
		db.execSQL("INSERT INTO templates VALUES(2,'Having continous dry cough, with throat irritation.')");
		db.execSQL("INSERT INTO templates VALUES(3,'Caught a fever, could not attend duty.')");
		db.execSQL("INSERT INTO templates VALUES(4,'I am having a severe headache')");
		db.execSQL("INSERT INTO templates VALUES(5,'Got a migraine attack.')");
		db.execSQL("INSERT INTO templates VALUES(6,'I have got a severe stomache ache. Salaam.')");
		db.execSQL("INSERT INTO templates VALUES(7,'Salaam, having a back pain. Going to see a doc.')");
		db.execSQL("INSERT INTO templates VALUES(8,'Feeling nausea.')");
		db.execSQL("INSERT INTO templates VALUES(9,'Balive')");
		db.execSQL("INSERT INTO templates VALUES(10,'Salaam, I am sick.')");
		db.execSQL("INSERT INTO templates VALUES(11,'Adhives balive')");
		db.execSQL("INSERT INTO templates VALUES(12,'I am having breathing difficulty, going to see the doc today.')");
		db.execSQL("INSERT INTO templates VALUES(13,'Severe Headche, salaam.')");
		db.execSQL("INSERT INTO templates VALUES(14,'Got an allergy, need to see the doc. salaam.')");
		db.execSQL("INSERT INTO templates VALUES(15,'I got loose motion and vomiting, could not attend duty, sorry.')");
		db.execSQL("INSERT INTO templates VALUES(16,'Salaam.')");
		db.execSQL("INSERT INTO templates VALUES(17,'Adhives varah hungadha.')");
		db.execSQL("INSERT INTO templates VALUES(18,'Due to the cold weather, my joints are paining. Could not goto duty " +
				"today.')");
		db.execSQL("INSERT INTO templates VALUES(19,'Got Upper respiratory track infection (URTI). Salaam.')");
		db.execSQL("INSERT INTO templates VALUES(20,'Severe lowwer abdomen pain, salaam.')");
	}

	public SalaamDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

}
